package com.customer.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountResponse {
    private Long accountId;
    private Long customerId;
    private double balance;

    // Getters, Setters, Constructors
}
