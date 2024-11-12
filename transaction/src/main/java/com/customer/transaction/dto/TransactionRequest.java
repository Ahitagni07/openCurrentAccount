package com.customer.transaction.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionRequest {

    @Valid

    @NotNull
    @Positive(message = "accountId must be positive")
    private Long accountId;
    @Positive(message = "Amount must be positive")
    @NotNull
    private Double amount;
}