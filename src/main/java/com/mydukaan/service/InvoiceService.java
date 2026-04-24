package com.mydukaan.service;

import com.mydukaan.dto.common.ApiResponse;
import com.mydukaan.dto.request.InvoiceItemRequest;
import com.mydukaan.dto.request.InvoiceRequest;
import com.mydukaan.dto.response.InvoiceItemResponse;
import com.mydukaan.dto.response.InvoiceResponse;
import com.mydukaan.dto.response.PaymentResponse;
import com.mydukaan.enums.InvoiceStatus;
import com.mydukaan.enums.TransactionType;
import com.mydukaan.exception.ResourceNotFoundException;
import com.mydukaan.model.*;
import com.mydukaan.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class InvoiceService {
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;
    private final InvoiceRepository invoiceRepo;
    private final StoreRepository storeRepo;
    private final CustomerRepository customerRepo;
    private final ProductRepository productRepo;
    private final InvoiceItemRepository invoiceItemRepo;
    private final LedgerRepository ledgerRepo;
    private final PaymentRepository paymentRepo;
    private final LedgerEntriesRepository ledgerEntriesRepo;

    public InvoiceService(InvoiceRepository invoiceRepo,
                          StoreRepository storeRepo,
                          CustomerRepository customerRepo,
                          ProductRepository productRepo,
                          InvoiceItemRepository invoiceItemRepo,
                          LedgerRepository ledgerRepo, PaymentRepository paymentRepo,
                          LedgerEntriesRepository ledgerEntriesRepo) {
        this.invoiceRepo = invoiceRepo;
        this.storeRepo = storeRepo;
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;
        this.invoiceItemRepo = invoiceItemRepo;
        this.ledgerRepo = ledgerRepo;
        this.paymentRepo = paymentRepo;
        this.ledgerEntriesRepo = ledgerEntriesRepo;
    }

    @Transactional
    public ApiResponse<InvoiceResponse> createInvoice(InvoiceRequest request) {

        Store store = getStore(request.getStoreId());
        Customer customer = getCustomer(request.getCustomerId());

        Invoice invoice = buildInvoice(request, store, customer);
        invoice.setCreatedAt(request.getCreatedAt());
        List<InvoiceItem> items = processItems(request.getItems(), invoice);
        BigDecimal subtotal = calculateSubtotal(items);

        TaxResult tax = calculateTax(subtotal);

        invoice.setSubTotal(subtotal);
        invoice.setGstAmount(tax.gst());
        invoice.setTotalAmount(tax.total());

        Invoice savedInvoice = invoiceRepo.save(invoice);

        saveItems(items, savedInvoice);

        PaymentResult paymentResult = processPayments(request, savedInvoice);

        updateInvoiceStatus(savedInvoice, paymentResult.paidAmount(), tax.total());
        updateCustomerBalance(customer, tax.total(), paymentResult.paidAmount());

        savedInvoice.setPaidAmount(paymentResult.paidAmount());
        savedInvoice.setDueAmount(tax.total().subtract(paymentResult.paidAmount()));

        return new ApiResponse<>(true, "Invoice created", mapToFullResponse(invoiceRepo.save(savedInvoice)));
    }

    @Transactional(readOnly = true)
    public ApiResponse<InvoiceResponse> getInvoiceById(Long id) {

        Invoice invoice = invoiceRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        return new ApiResponse<>(true,
                "Invoice fetched successfully",
                mapToFullResponse(invoice)
        );
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<InvoiceResponse>> getAllInvoices(Long storeId) {
        List<Invoice> invoices = invoiceRepo.findByStoreId(storeId);
        List<InvoiceResponse> responses = invoices.stream().map(this::mapToFullResponse).toList();
        return new ApiResponse<>(true, "Successfully!!!", responses);
    }

    @Transactional
    public ApiResponse<Void> deleteInvoice(Long id) {
        System.out.println(id);
        Invoice invoice = invoiceRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        Customer customer = invoice.getCustomer();

        // Reverse customer balance
        if (customer != null) {
            customer.setBalance(customer.getBalance().subtract(invoice.getDueAmount()));
        }

        // Reverse payments
        for (Payment payment : invoice.getPayments()) {
            Ledger ledger = payment.getLedger();
            ledger.setBalance(ledger.getBalance().subtract(payment.getAmount()));
            ledgerEntriesRepo.deleteByPaymentId(payment.getId());
        }

        // Restore stock
        for (InvoiceItem item : invoice.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
        }

        invoiceRepo.deleteById(invoice.getId());

        return new ApiResponse<>(true, "Invoice deleted", null);
    }

    @Transactional
    public ApiResponse<InvoiceResponse> updateInvoice(Long id, InvoiceRequest request) {

        // Step 1: delete old (reversal)
        deleteInvoice(id);

        ApiResponse<InvoiceResponse> response = createInvoice(request);
        return new ApiResponse<>(true, "Updated Successfully!!!", response.getData());
    }

    private Invoice buildInvoice(InvoiceRequest request, Store store, Customer customer) {
        return Invoice.builder()
                .invoiceNumber(
                        request.getInvoiceNumber() != null
                                ? request.getInvoiceNumber()
                                : generateInvoiceNumber()
                )
                .store(store)
                .customer(customer)
                .status(InvoiceStatus.PENDING)
                .build();
    }

    private List<InvoiceItem> processItems(List<InvoiceItemRequest> requests, Invoice invoice) {

        List<InvoiceItem> items = new ArrayList<>();
        List<Product> updatedProducts = new ArrayList<>();

        for (InvoiceItemRequest req : requests) {

            Product product = getProduct(req.getProductId());

            if (product.getStock() < req.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for " + product.getName());
            }

            product.setStock(product.getStock() - req.getQuantity());
            updatedProducts.add(product);

            items.add(InvoiceItem.builder()
                    .invoice(invoice)
                    .product(product)
                    .quantity(req.getQuantity())
                    .price(product.getPrice())
                    .build());
        }

        productRepo.saveAll(updatedProducts);

        return items;
    }

    private PaymentResult processPayments(InvoiceRequest request, Invoice invoice) {
        if (invoice.getId() == null) {
            throw new IllegalStateException("Invoice must be saved before processing payments");
        }
        if (request.getPayments() == null || request.getPayments().isEmpty()) {
            return new PaymentResult(BigDecimal.ZERO);
        }

        BigDecimal paidAmount = BigDecimal.ZERO;
        List<Payment> payments = new ArrayList<>();

        for (var payReq : request.getPayments()) {

            Ledger ledger = ledgerRepo.findById(payReq.getLedgerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ledger not found"));

            Payment payment = new Payment();
            payment.setInvoice(invoice);
            payment.setLedger(ledger);
            payment.setAmount(payReq.getAmount());
            payment.setMethod(payReq.getMethod());
            payment.setTransactionId(payReq.getTransactionId());
            payment.setStatus("SUCCESS");
            payment.setCreatedAt(payment.getCreatedAt() == null ? invoice.getCreatedAt() : payment.getCreatedAt());
            payments.add(payment);

            // Ledger update
            ledger.setBalance(ledger.getBalance().add(payReq.getAmount()));

            paidAmount = paidAmount.add(payReq.getAmount());
        }

        invoice.setPayments(paymentRepo.saveAll(payments));

        invoice.getPayments().forEach(payment -> {
            LedgerEntry ledgerEntry = LedgerEntry.builder()
                    .name(invoice.getCustomer().getName())
                    .type(TransactionType.PAYMENT_IN)
                    .payment(payment)
                    .ledger(payment.getLedger())
                    .amount(payment.getAmount())
                    .createdAt(payment.getCreatedAt() != null ? payment.getCreatedAt() : invoice.getCreatedAt())
                    .build();
            ledgerEntriesRepo.save(ledgerEntry);
        });
        return new PaymentResult(paidAmount);
    }

    public ApiResponse<List<InvoiceResponse>> getInvoiceByCustomer(Long customerId) {
        List<Invoice> invoices = invoiceRepo.findByCustomerId(customerId);
        List<InvoiceResponse> responses = invoices.stream().map(this::mapToFullResponse).toList();
        return new ApiResponse<>(true, "Success", responses);
    }

    private record TaxResult(BigDecimal gst, BigDecimal total) {
    }

    private void saveItems(List<InvoiceItem> items, Invoice invoice) {
        items.forEach(i -> i.setInvoice(invoice));
        invoice.setItems(invoiceItemRepo.saveAll(items));
    }

    private TaxResult calculateTax(BigDecimal subtotal) {
        BigDecimal gst = subtotal
                .multiply(BigDecimal.valueOf(0.18))
                .setScale(SCALE, ROUNDING);

        BigDecimal total = subtotal
                .add(gst)
                .setScale(SCALE, ROUNDING);

        return new TaxResult(gst, total);
    }

    private BigDecimal calculateSubtotal(List<InvoiceItem> items) {
        return items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private record PaymentResult(BigDecimal paidAmount) {
    }

    private void updateInvoiceStatus(Invoice invoice, BigDecimal paid, BigDecimal total) {

        if (paid.compareTo(BigDecimal.ZERO) == 0) {
            invoice.setStatus(InvoiceStatus.PENDING);
        } else if (paid.compareTo(total) < 0) {
            invoice.setStatus(InvoiceStatus.PARTIAL);
        } else {
            invoice.setStatus(InvoiceStatus.PAID);
        }
    }

    private void updateCustomerBalance(Customer customer, BigDecimal total, BigDecimal paid) {

        if (customer == null) return;

        BigDecimal due = total.subtract(paid);
        customer.setBalance(customer.getBalance().add(due));
    }

    // 🔧 HELPERS
    private Store getStore(Long id) {
        return storeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
    }

    private Customer getCustomer(Long id) {
        if (id == null) return null;
        return customerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    private Product getProduct(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    private String generateInvoiceNumber() {
        return "INV-" + LocalDate.now() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private InvoiceResponse mapToFullResponse(
            Invoice invoice
    ) {

        return InvoiceResponse.builder()
                .id(invoice.getId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .storeId(invoice.getStore().getId())
                .subTotal(invoice.getSubTotal())
                .customerId(invoice.getCustomer() != null ? invoice.getCustomer().getId() : null)
                .customerName(invoice.getCustomer() != null ? invoice.getCustomer().getName() : null)
                .totalAmount(invoice.getTotalAmount())
                .dueAmount(invoice.getDueAmount())
                .paidAmount(invoice.getPaidAmount())
                .gstAmount(invoice.getGstAmount())
                .status(invoice.getStatus())
                .createdAt(invoice.getCreatedAt())

                // ITEMS
                .items(invoice.getItems().stream().map(item ->
                        InvoiceItemResponse.builder()
                                .id(item.getId())
                                .productId(item.getProduct().getId())
                                .productName(item.getProduct().getName())
                                .quantity(item.getQuantity())
                                .price(item.getPrice())
                                .total(item.getPrice()
                                        .multiply(java.math.BigDecimal.valueOf(item.getQuantity())))
                                .build()
                ).toList())

                // PAYMENTS
                .payments(invoice.getPayments().stream().map(payment ->
                        PaymentResponse.builder()
                                .id(payment.getId())
                                .invoiceId(invoice.getId())
                                .invoiceNumber(invoice.getInvoiceNumber())
                                .ledgerId(payment.getLedger().getId())
                                .ledgerName(payment.getLedger().getDisplayName())
                                .amount(payment.getAmount())
                                .method(payment.getMethod())
                                .status(payment.getStatus())
                                .transactionId(payment.getTransactionId())
                                .createdAt(payment.getCreatedAt())
                                .build()
                ).toList())
                .build();
    }

}