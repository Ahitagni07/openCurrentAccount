package com.customer.transaction.dto;

import lombok.Data;

@Data
public class TransactionInfo {
    private Long trxId;
    private double amount;
}
