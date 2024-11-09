package com.customer.account.dto;

public class TransactionInfo {
    private Long transactionId;
    private double amount;

    public TransactionInfo(Long transactionId, double amount) {
        this.transactionId = transactionId;
        this.amount = amount;
    }

    // Getters and setters
}
