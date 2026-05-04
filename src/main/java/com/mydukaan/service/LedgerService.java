package com.mydukaan.service;

import com.mydukaan.dto.common.ApiResponse;
import com.mydukaan.dto.request.LedgerAdjRequest;
import com.mydukaan.dto.request.LedgerRequest;
import com.mydukaan.dto.response.LedgerResponse;
import com.mydukaan.dto.response.PaymentResponse;
import com.mydukaan.enums.TransactionType;
import com.mydukaan.exception.ResourceNotFoundException;
import com.mydukaan.model.Ledger;
import com.mydukaan.model.Payment;
import com.mydukaan.model.Store;
import com.mydukaan.repository.LedgerRepository;
import com.mydukaan.repository.PaymentRepository;
import com.mydukaan.repository.StoreRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class LedgerService {
    private final LedgerRepository repo;
    private final StoreRepository storeRepo;
    private final PaymentRepository paymentRepo;

    LedgerService(LedgerRepository repo, StoreRepository storeRepo,
                  PaymentRepository paymentRepo) {
        this.repo = repo;
        this.storeRepo = storeRepo;
        this.paymentRepo = paymentRepo;
    }

    public ApiResponse<LedgerResponse> createLedger(LedgerRequest ledgerRequest) {
        Ledger l = new Ledger();
        Store store = storeRepo.findById(ledgerRequest.getStoreId()).orElseThrow(() ->
                new ResourceNotFoundException("There is no store associated with this id"));
        l.setStore(store);
        l.setDisplayName(ledgerRequest.getDisplayName());
        l.setBalance(ledgerRequest.getBalance());
        l.setAccountType(ledgerRequest.getAccountType());

        Ledger saved = repo.save(l);
        LedgerResponse res = mapToResponse(saved);

        Payment payment = Payment.builder()
                .name("Opening Balance")
                .ledger(l)
                .amount(ledgerRequest.getBalance())
                .status("SUCCESS")
                .type(TransactionType.OPENING_BALANCE)
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepo.save(payment);

        return new ApiResponse<>(true, "Ledger created Successfully!!!", res);
    }

    public ApiResponse<List<LedgerResponse>> getLedgers(Long storeId) {
        List<Ledger> ledgers = repo.findByStoreId(storeId).orElseThrow(() ->
                new ResourceNotFoundException("There is no store associated with this id"));
        List<LedgerResponse> res = ledgers.stream().map(this::mapToResponse).toList();

        return new ApiResponse<>(true, "Ledger fetched Successfully!!!", res);
    }

    public ApiResponse<LedgerResponse> updateLedger(LedgerRequest ledgerRequest, Long ledgerId) {
        Ledger ledger = repo.findById(ledgerId).orElseThrow(() -> new RuntimeException("Not Found!"));

        if (ledgerRequest.getAccountType() != null) {
            ledger.setAccountType(ledgerRequest.getAccountType());
        }

        if (ledgerRequest.getBalance() != null) {
            ledger.setBalance(ledgerRequest.getBalance());
        }

        if (ledgerRequest.getDisplayName() != null) {
            ledger.setDisplayName(ledgerRequest.getDisplayName());
        }

        Ledger updatedLedger = repo.save(ledger);
        Payment payment = paymentRepo.findOpeningBalanceByLedgerId(ledger.getId());
        if (payment == null) {
            payment = Payment.builder()
                    .name(
                            getLedgerName(TransactionType.OPENING_BALANCE))
                    .type(TransactionType.OPENING_BALANCE)
                    .ledger(ledger)
                    .amount(ledgerRequest.getBalance())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        } else if (!Objects.equals(ledgerRequest.getBalance(), payment.getAmount())) {
            payment.setAmount(ledger.getBalance());
        }
        payment.setCreatedAt(updatedLedger.getCreatedAt());
        paymentRepo.save(payment);
        return new ApiResponse<>(true, "Ledger updated Successfully!!!", mapToResponse(updatedLedger));
    }

    public ApiResponse<List<PaymentResponse>> getLedgerEntries(Long ledgerId) {
        List<Payment> ledgerEntries = paymentRepo.findByLedgerId(ledgerId);
        List<PaymentResponse> responses = ledgerEntries.stream().map(this::mapToPaymentResponse).toList();
        return new ApiResponse<>(true, "Fetched all entries", responses);
    }

    public ApiResponse<?> updateLedgerAdj(Long ledgerId, LedgerAdjRequest ledgerAdjRequest) {
        Ledger ledger = repo.findById(ledgerId).orElseThrow(() -> new ResourceNotFoundException("Ledger not found!!"));
        if (ledgerAdjRequest.getType() == TransactionType.ADJUSTMENT_INCREASE) {
            ledger.setBalance(ledger.getBalance().add(ledgerAdjRequest.getAmount()));
        } else {
            ledger.setBalance(ledger.getBalance().subtract(ledgerAdjRequest.getAmount()));
        }
        Payment payment = Payment.builder()
                .name(getLedgerName(ledgerAdjRequest.getType()))
                .type(ledgerAdjRequest.getType())
                .ledger(ledger)
                .amount(ledgerAdjRequest.getAmount())
                .createdAt(ledgerAdjRequest.getCreatedAt())
                .build();
        paymentRepo.save(payment);
        return new ApiResponse<>(true, "Updated Successfully!!!", null);
    }

    public ApiResponse<?> accountToAccount(List<LedgerAdjRequest> ledgerAdjRequests) {
        if (ledgerAdjRequests.size() != 2) {
            throw new RuntimeException("Something went wrong!!");
        }
        updateLedgerAdj(ledgerAdjRequests.getFirst().getLedgerId(), ledgerAdjRequests.getFirst());
        updateLedgerAdj(ledgerAdjRequests.get(1).getLedgerId(), ledgerAdjRequests.get(1));
        return new ApiResponse<>(true, "Transferred Successfully!!!", null);
    }

    private String getLedgerName(TransactionType type) {
        return switch (type) {
            case OPENING_BALANCE -> "Opening Balance";
            case CREDIT -> "Credit";
            case DEBIT -> "Debit";
            case SALE -> "Sale";
            case PURCHASE -> "Purchase";
            case PAYMENT_IN -> "Payment Received";
            case PAYMENT_OUT -> "Payment Sent";
            case REFUND -> "Refund";
            case ADJUSTMENT_INCREASE -> "Adjustment Increase";
            case ADJUSTMENT_DECREASE -> "Adjustment Decrease";
        };
    }

    private LedgerResponse mapToResponse(Ledger ledger) {
        return LedgerResponse.builder()
                .ledgerId(ledger.getId())
                .displayName(ledger.getDisplayName())
                .storeId(ledger.getStore().getId())
                .accountType(ledger.getAccountType())
                .balance(ledger.getBalance())
                .transactions(
                        ledger.getTransactions().stream().map(this::mapToPaymentResponse).toList()
                )
                .build();
    }

    public ApiResponse<?> deleteLedger(Long ledgerId) {
        repo.deleteById(ledgerId);
        return new ApiResponse<>(true, "Deleted Successfully!!!", null);
    }

    private PaymentResponse mapToPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .name(payment.getName())
                .invoiceId(payment.getInvoice() != null ? payment.getInvoice().getId() : null)
                .invoiceNumber(payment.getInvoice() != null ? payment.getInvoice().getInvoiceNumber() : null)
                .ledgerId(payment.getLedger().getId())
                .ledgerName(payment.getLedger().getDisplayName())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .type(getLedgerName(payment.getType()))
                .transactionId(payment.getTransactionId())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
