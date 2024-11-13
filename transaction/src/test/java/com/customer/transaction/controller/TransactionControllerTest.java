package com.customer.transaction.controller;

import com.customer.transaction.dto.TransactionInfo;
import com.customer.transaction.dto.TransactionRequest;
import com.customer.transaction.exceptionhandler.AccountNotFoundException;
import com.customer.transaction.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableWebMvc
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void testCreateTransaction() throws Exception {
        // Arrange
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAccountId(1L);
        transactionRequest.setAmount(100.0);

        // Act & Assert
        mockMvc.perform(post("/api/transactions/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountId\":1,\"amount\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction is created for account Id 1"));

        // Verify that the service method was called once
        verify(transactionService, times(1)).createTransaction(1L, 100.0);
    }

    @Test
    void testGetTransactionsByAccountId() throws Exception {
        // Arrange
        Long accountId = 1L;
        List<TransactionInfo> transactionInfoList = List.of(
                new TransactionInfo(1L, 50.0),
                new TransactionInfo(2L, 100.0)
        );
        when(transactionService.getTransactionsByAccountId(accountId)).thenReturn(Optional.of(transactionInfoList));

        // Act & Assert
        mockMvc.perform(get("/api/transactions/account/{accountId}", accountId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"trxId\":1,\"amount\":50.0}," +
                        "{\"trxId\":2,\"amount\":100.0}]"));

        // Verify that the service method was called once
        verify(transactionService, times(1)).getTransactionsByAccountId(accountId);
    }

//    @Test
//    void testGetTransactionsByAccountId_AccountNotFound() throws Exception {
//
//        Long accountId = 999L;
//        when(transactionService.getTransactionsByAccountId(accountId)).thenReturn(Optional.empty());
//
//        when(transactionController.getTransactionsByAccountId(accountId)).thenThrow(new AccountNotFoundException("Account not found"));
//
//        mockMvc.perform(get("/api/transactions/account/{accountId}", accountId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.message").value("Account not found"));
//
//        verify(transactionController, times(1)).getTransactionsByAccountId(accountId);
//    }
}