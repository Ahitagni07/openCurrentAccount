package com.customer.account.service;

import com.customer.account.dto.*;
import com.customer.account.entity.Account;
import com.customer.account.entity.User;
import com.customer.account.repository.AccountRepository;
import com.customer.account.repository.UserRepository;
import jakarta.transaction.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Account openAccount(Long customerId, double initialCredit) {

        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = new Account();
        account.setUser(user);
        account.setBalance(initialCredit);
        Account savedAccount = accountRepository.save(account);

        // If initial credit is provided, create a transaction in the Transaction Service
        if (initialCredit > 0) {
            TransactionRequest transactionRequest = new TransactionRequest(account.getId(), initialCredit);
            restTemplate.postForObject("http://localhost:8081/api/transactions/create", transactionRequest, Void.class);
        }
        return new Account(savedAccount.getId(), user, savedAccount.getBalance());
    }

    public UserInfoResponse getAccountInfo(Long customerId) {

        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserInfoResponse response = new UserInfoResponse();
        response.setName(user.getName());
        response.setSurname(user.getSurname());

        double totalBalance = 0.0;
        List<Account> accounts = accountRepository.findByUserId(user);

        for (Account account : accounts) {
            AccountInfo accountInfo = new AccountInfo();
            accountInfo.setAccountId(account.getId());
            accountInfo.setBalance(account.getBalance());
            totalBalance += account.getBalance();

            // Retrieve transactions for each account from the Transaction Service
            ResponseEntity<List<TransactionInfo>> transactionInfoResp = restTemplate.exchange(
                    "http://localhost:8081/transactions/account/" + account.getId(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<TransactionInfo>>() {});

            List<TransactionInfo> transactionInfoList = new ArrayList<>();
            if(transactionInfoResp.getStatusCode().is2xxSuccessful()){
                transactionInfoList = transactionInfoResp.getBody();
            }
//            List<TransactionInfo> transactionInfoList = transactions.stream()
//                    .map(t -> new TransactionInfo(t.getId(), t.getAmount()))
//                    .collect(Collectors.toList());

            accountInfo.setTransactions(transactionInfoList);
            response.getAccounts().add(accountInfo);
        }

        response.setBalance(totalBalance);
        return response;
    }
}
