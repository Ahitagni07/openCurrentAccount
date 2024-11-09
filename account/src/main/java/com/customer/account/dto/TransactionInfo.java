package com.customer.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionInfo {
    private Long trxId;
    private double amount;
}

