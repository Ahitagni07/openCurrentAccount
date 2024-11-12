package com.customer.account.controller;

import com.customer.account.dto.UserInfoResponse;
import com.customer.account.entity.Account;
import com.customer.account.exceptionhandler.CustomerNotFoundException;
import com.customer.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping(value = "/open", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> openAccount(@RequestParam Long customerID, @RequestParam double initialCredit) {
        try {
            Optional<Account> account = accountService.openAccount(customerID, initialCredit);
            if (account.isPresent()) {
                return ResponseEntity.ok("Account created successfully with accountId: " + account.get().getId() + " for customer Id " + customerID);
            } else {
                // Account was not created, possibly due to invalid input or business rules
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Account could not be created for customer Id " + customerID);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/userInfo")
    public ResponseEntity<UserInfoResponse> getAccountInfo(@RequestParam Long customerID) throws CustomerNotFoundException {
        Optional<UserInfoResponse> userInfo = accountService.getAccountInfo(customerID);
        if(userInfo.isPresent()) {
            return ResponseEntity.ok(userInfo.get());
        }
        throw new CustomerNotFoundException();
    }
}
