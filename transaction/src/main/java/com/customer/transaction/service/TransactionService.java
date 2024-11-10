package com.customer.transaction.service;

import com.customer.transaction.dto.TransactionInfo;
import com.customer.transaction.entity.Transaction;
import com.customer.transaction.exceptionhandler.AccountNotFoundException;
import com.customer.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction createTransaction(Long accountID, double amount) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountID);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    public Optional<List<TransactionInfo>> getTransactionsByAccountId(Long accountID) throws AccountNotFoundException {
        List<Transaction> transactionList = transactionRepository.findByAccountId(accountID);
        if(transactionList.isEmpty()) {
            throw new AccountNotFoundException("Account not found with accountId : "+ accountID);
        }
        List<TransactionInfo> transactionInfoList = new ArrayList<>();
        transactionList.forEach(transaction -> {
            TransactionInfo transactionInfo = new TransactionInfo();
            transactionInfo.setTrxId(transaction.getId());
            transactionInfo.setAmount(transaction.getAmount());
            transactionInfoList.add(transactionInfo);
        });
        return Optional.of(transactionInfoList);
    }
}
