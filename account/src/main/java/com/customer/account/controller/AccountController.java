package com.customer.account.controller;

import com.customer.account.dto.AccountResponse;
import com.customer.account.dto.UserInfoResponse;
import com.customer.account.entity.Account;
import com.customer.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/open")
    public ResponseEntity<String> openAccount(@RequestParam Long customerID, @RequestParam double initialCredit) {
        Account account = accountService.openAccount(customerID, initialCredit);
        return ResponseEntity.ok("Account created successfully with customerId: " + account.getId());
    }

    @GetMapping("/userInfo")
    public ResponseEntity<UserInfoResponse> getAccountInfo(@RequestParam Long customerID) {
        UserInfoResponse userInfo = accountService.getAccountInfo(customerID);
        return ResponseEntity.ok(userInfo);
    }
}
