package com.customer.transaction.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {

    @Valid

    @NotNull
    @Positive(message = "Kindly check with valid account detail")
    private Long accountId;
    @Positive(message = "Amount must be positive")
    @NotNull
    private Double amount;
}