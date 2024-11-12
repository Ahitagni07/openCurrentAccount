package com.customer.transaction.controller;

import com.customer.transaction.dto.TransactionInfo;
import com.customer.transaction.dto.TransactionRequest;
import com.customer.transaction.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void testCreateTransactionSuccess() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest(1L, 1000.0);

        doNothing().when(transactionService).createTransaction(1L, 1000.0);

        mockMvc.perform(post("/api/transactions/create")
                        .contentType("application/json")
                        .content("{\"accountId\": 1, \"amount\": 1000.0}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction is created for account Id 1"));

        verify(transactionService, times(1)).createTransaction(1L, 1000.0);
    }

    @Test
    void testCreateTransactionInvalidAmount() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest(1L, -1000.0);

        mockMvc.perform(post("/api/transactions/create")
                        .contentType("application/json")
                        .content("{\"accountId\": 1, \"amount\": -1000.0}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Amount must be positive"));

        verify(transactionService, never()).createTransaction(1L, -1000.0);
    }

    @Test
    void testCreateTransactionMissingAccountId() throws Exception {
        mockMvc.perform(post("/api/transactions/create")
                        .contentType("application/json")
                        .content("{\"amount\": 1000.0}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Account ID is required"));

        verify(transactionService, never()).createTransaction(anyLong(), anyDouble());
    }

    @Test
    void testGetTransactionsByAccountIdSuccess() throws Exception {
        Long accountId = 1L;
        TransactionInfo transaction1 = new TransactionInfo(1L, 500.0);
        TransactionInfo transaction2 = new TransactionInfo(2L, 200.0);

        when(transactionService.getTransactionsByAccountId(accountId)).thenReturn(Optional.of(Arrays.asList(transaction1, transaction2)));

        mockMvc.perform(get("/api/transactions/account/{accountId}", accountId))
                .andExpect(status().isOk())
                .andExpect(content().json("[{" +
                        "\"transactionId\": 1, \"accountId\": 1, \"amount\": 500.0, \"type\": \"credit\"}," +
                        "{\"transactionId\": 2, \"accountId\": 1, \"amount\": 200.0, \"type\": \"debit\"}]"));

        verify(transactionService, times(1)).getTransactionsByAccountId(accountId);
    }

    @Test
    void testGetTransactionsByAccountIdNotFound() throws Exception {
        Long accountId = 1L;

        when(transactionService.getTransactionsByAccountId(accountId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/transactions/account/{accountId}", accountId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Account not found"));

        verify(transactionService, times(1)).getTransactionsByAccountId(accountId);
    }

    @Test
    void testGetTransactionsByAccountIdInvalidAccountId() throws Exception {
        Long invalidAccountId = -1L;

        mockMvc.perform(get("/api/transactions/account/{accountId}", invalidAccountId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid account ID"));

        verify(transactionService, never()).getTransactionsByAccountId(invalidAccountId);
    }

    @Test
    void testCreateTransactionInvalidRequestBody() throws Exception {
        mockMvc.perform(post("/api/transactions/create")
                        .contentType("application/json")
                        .content("{\"accountId\": \"abc\", \"amount\": \"xyz\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid input"));

        verify(transactionService, never()).createTransaction(anyLong(), anyDouble());
    }
}