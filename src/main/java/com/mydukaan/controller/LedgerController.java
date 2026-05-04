package com.mydukaan.controller;

import com.mydukaan.dto.common.ApiResponse;
import com.mydukaan.dto.request.LedgerAdjRequest;
import com.mydukaan.dto.request.LedgerRequest;
import com.mydukaan.service.LedgerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ledger/")
public class LedgerController {
    private final LedgerService service;

    public LedgerController(LedgerService service) {
        this.service = service;
    }

    @PostMapping("/create-ledger")
    public ResponseEntity<ApiResponse> createLedger(@RequestBody LedgerRequest body) {
        return ResponseEntity.status(201).body(service.createLedger(body));
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResponse> getLedgers(@PathVariable Long storeId) {
        return ResponseEntity.status(200).body(service.getLedgers(storeId));
    }

    @PutMapping("/{ledgerId}")
    public ResponseEntity<ApiResponse> updateLedger(@PathVariable Long ledgerId, @RequestBody LedgerRequest body) {
        return ResponseEntity.status(201).body(service.updateLedger(body, ledgerId));
    }

    @PutMapping("/update-amount/{ledgerId}")
    public ResponseEntity<ApiResponse> updateAmount(@PathVariable Long ledgerId, @RequestBody LedgerAdjRequest ledgerAdjRequest) {
        return ResponseEntity.status(200).body(service.updateLedgerAdj(ledgerId, ledgerAdjRequest));
    }

    @PutMapping("/transfer")
    public ResponseEntity<ApiResponse> transferAccountToAccount(@RequestBody List<LedgerAdjRequest> ledgerAdjRequest) {
        return ResponseEntity.status(200).body(service.accountToAccount(ledgerAdjRequest));
    }

    @GetMapping("/transaction/{ledgerId}")
    public ResponseEntity<ApiResponse> getLedgerEntries(@PathVariable Long ledgerId) {
        return ResponseEntity.status(200).body(service.getLedgerEntries(ledgerId));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteLedger(@RequestParam Long ledgerId) {
        return ResponseEntity.status(201).body(service.deleteLedger(ledgerId));
    }
}
