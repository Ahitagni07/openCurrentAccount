package com.customer.transaction.controller;

import com.customer.transaction.dto.TransactionInfo;
import com.customer.transaction.dto.TransactionRequest;
import com.customer.transaction.entity.Transaction;
import com.customer.transaction.exceptionhandler.AccountNotFoundException;
import com.customer.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<String> createTransaction(@RequestBody TransactionRequest transactionRequest) {
        transactionService.createTransaction(transactionRequest.getAccountId(), transactionRequest.getAmount());
        return ResponseEntity.ok("Transaction is created for account Id "+transactionRequest.getAccountId());
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionInfo>> getTransactionsByAccountId(@PathVariable Long accountId) throws AccountNotFoundException {
        Optional<List<TransactionInfo>> transactionInfoList = transactionService.getTransactionsByAccountId(accountId);
        if(transactionInfoList.isPresent()) {
            return ResponseEntity.ok(transactionInfoList.get());
        }
        throw new AccountNotFoundException();
    }
}
