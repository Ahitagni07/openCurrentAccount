package com.customer.account.service;

import com.customer.account.dto.AccountResponse;
import com.customer.account.dto.UserInfoResponse;
import com.customer.account.entity.Account;

public interface AccountService {
    Account openAccount(Long customerId, double initialCredit);
    UserInfoResponse getAccountInfo(Long customerId);
}
