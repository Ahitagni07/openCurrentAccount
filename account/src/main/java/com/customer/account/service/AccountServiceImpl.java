package com.customer.account.service;

import com.customer.account.dto.AccountResponse;
import com.customer.account.entity.Account;
import com.customer.account.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public AccountResponse openAccount(Long customerId, double initialCredit) {
        Account account = new Account();
        account.setCustomerId(customerId);
        account.setBalance(initialCredit);
        Account savedAccount = accountRepository.save(account);

        return new AccountResponse(savedAccount.getId(), savedAccount.getCustomerId(), savedAccount.getBalance());
    }

    public AccountResponse getAccountInfo(Long customerId) {
        // Fetch and convert to DTO for response
        return null;
    }
}
