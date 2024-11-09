package com.customer.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/open")
    public Account openAccount(@RequestParam Long customerID, @RequestParam double initialCredit) {
        return accountService.createAccount(customerID, initialCredit);
    }

    @GetMapping("/{customerID}")
    public Account getAccountInfo(@PathVariable Long customerID) {
        return accountService.getAccountByCustomerID(customerID);
    }
}
