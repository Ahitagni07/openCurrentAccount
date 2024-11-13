package com.customer.transaction.service;

import com.customer.transaction.dto.TransactionInfo;
import com.customer.transaction.entity.Transaction;
import com.customer.transaction.exceptionhandler.AccountNotFoundException;
import com.customer.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * this method is getting called from account service to create transaction when a account is created
     * with non-zero initial credit.
     *
     * @param accountID
     * @param amount
     */
    public void createTransaction(Long accountID, Double amount) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountID);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    /**
     * this method is used to get accountinfo with passing valid accountid.
     * @param accountID
     * @return
     * @throws AccountNotFoundException
     */
    public Optional<List<TransactionInfo>> getTransactionsByAccountId(Long accountID) throws AccountNotFoundException {
        List<Transaction> transactionList = transactionRepository.findByAccountId(accountID);
        if (transactionList.isEmpty()) {
            throw new AccountNotFoundException("Account not found with accountId : " + accountID);
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
