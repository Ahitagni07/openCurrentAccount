package com.customer.transaction.controller;

import com.customer.transaction.dto.TransactionInfo;
import com.customer.transaction.dto.TransactionRequest;
import com.customer.transaction.exceptionhandler.AccountNotFoundException;
import com.customer.transaction.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    /**
     * this api is getting called from account service to create transaction when a account is created
     * with non-zero initial credit.
     *
     * @param transactionRequest
     * @return
     */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createTransaction(@RequestBody @Valid TransactionRequest transactionRequest) {
        transactionService.createTransaction(transactionRequest.getAccountId(), transactionRequest.getAmount());
        return ResponseEntity.ok("Transaction is created for account Id " + transactionRequest.getAccountId());
    }

    /**
     * this api is used to get accountinfo with passing valid accountid.
     *
     * @param accountId
     * @return
     * @throws AccountNotFoundException
     */
    @GetMapping(value = "/account/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTransactionsByAccountId(@PathVariable(required = true) @Positive(message = "Kindly check with valid account detail") Long accountId) throws AccountNotFoundException {
        Optional<List<TransactionInfo>> transactionInfoList = transactionService.getTransactionsByAccountId(accountId);
        if (transactionInfoList.isPresent()) {
            return ResponseEntity.ok(transactionInfoList.get());
        }
        throw new AccountNotFoundException();
    }
}
