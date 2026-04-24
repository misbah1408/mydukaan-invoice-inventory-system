package com.mydukaan.controller;

import com.mydukaan.dto.common.ApiResponse;
import com.mydukaan.dto.request.InvoiceRequest;
import com.mydukaan.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {
    private final InvoiceService service;

    public InvoiceController(InvoiceService service) {
        this.service = service;
    }

    @PostMapping("/create-invoice")
    public ResponseEntity<ApiResponse> createInvoice(@RequestBody InvoiceRequest invoice) {
        return ResponseEntity.status(201).body(service.createInvoice(invoice));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getInvoiceById(@PathVariable Long id){
        return ResponseEntity.status(200).body(service.getInvoiceById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse> getInvoiceByCustomer(@PathVariable Long customerId){
        return ResponseEntity.status(200).body(service.getInvoiceByCustomer(customerId));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse> getAllInvoices(@RequestParam Long storeId){
        return ResponseEntity.status(200).body(service.getAllInvoices(storeId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteInvoiceById(@PathVariable Long id){
        return ResponseEntity.status(200).body(service.deleteInvoice(id));
    }

    @PutMapping("/update/{invoiceId}")
    public ResponseEntity<ApiResponse> updateInvoice(@PathVariable Long invoiceId, @RequestBody InvoiceRequest invoiceRequest){
        System.out.println(invoiceId);
        return ResponseEntity.status(200).body(service.updateInvoice(invoiceId, invoiceRequest));
    }
}
