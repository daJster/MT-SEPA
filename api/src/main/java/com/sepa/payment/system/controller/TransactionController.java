package com.sepa.payment.system.controller;

import com.sepa.payment.system.entity.Transaction;
import com.sepa.payment.system.service.TransactionServiceFactory;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/transactions")
public class TransactionController {

    public final TransactionServiceFactory transactionServiceFactory;

    @Autowired
    public TransactionController(TransactionServiceFactory transactionServiceFactory) {
        this.transactionServiceFactory = transactionServiceFactory;
    }

    // post transaction
    @PostMapping("/process/ST")
    public ResponseEntity<Boolean> processTransactionSingleThread(@Valid @RequestBody Transaction t) {
        boolean respBool = transactionServiceFactory.getST().ProcessTransaction(t);
        return ResponseEntity.ok(respBool);
    }

    @PostMapping("/process/MT")
    public ResponseEntity<Boolean> processTransactionMultiThread(@Valid @RequestBody Transaction t) {
        boolean respBool = transactionServiceFactory.getMT().ProcessTransaction(t);
        return ResponseEntity.ok(respBool);
    }
}
