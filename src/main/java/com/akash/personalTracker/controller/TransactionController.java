package com.akash.personalTracker.controller;

import com.akash.personalTracker.dto.TransactionRequest;
import com.akash.personalTracker.dto.TransactionResponse;
import com.akash.personalTracker.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // POST /api/transactions — Create a transaction for the authenticated user
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody TransactionRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName(); // Extracted from JWT by JwtAuthenticationFilter
        TransactionResponse response = transactionService.createTransaction(request, email);
        return ResponseEntity.ok(response);
    }

    // GET /api/transactions — Fetch all transactions for the authenticated user
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getUserTransactions(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(transactionService.findTransactionsByUserEmail(email));
    }

    // GET /api/transactions/balance — Calculate net balance for the authenticated user
    @GetMapping("/balance")
    public ResponseEntity<Double> getNetBalance(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(transactionService.calculateNetBalanceByEmail(email));
    }
}