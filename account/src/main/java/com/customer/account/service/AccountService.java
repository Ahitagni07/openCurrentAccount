package com.customer.account.service;

import com.customer.account.dto.AccountResponse;

public interface AccountService {
    AccountResponse openAccount(Long customerId, double initialCredit);
    AccountResponse getAccountInfo(Long customerId);
}
