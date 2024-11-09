package com.customer.transaction.dto;

import lombok.Data;

@Data
public class TransactionInfo {
    private Long accountId;
    private double amount;
}
