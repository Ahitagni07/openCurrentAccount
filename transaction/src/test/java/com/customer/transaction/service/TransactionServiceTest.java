package com.customer.transaction.service;

import com.customer.transaction.dto.TransactionInfo;
import com.customer.transaction.entity.Transaction;
import com.customer.transaction.exceptionhandler.AccountNotFoundException;
import com.customer.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Long accountId;
    private Double amount;

    @BeforeEach
    void setUp() {
        accountId = 1L;
        amount = 100.0;
    }

    // Test case for createTransaction
    @Test
    void testCreateTransaction() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());

        // Act
        transactionService.createTransaction(accountId, amount);

        // Assert
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    // Test case for getTransactionsByAccountId when transactions are found
    @Test
    void testGetTransactionsByAccountId_Success() throws AccountNotFoundException {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);

        when(transactionRepository.findByAccountId(accountId)).thenReturn(transactionList);

        // Act
        Optional<List<TransactionInfo>> result = transactionService.getTransactionsByAccountId(accountId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        assertEquals(amount, result.get().get(0).getAmount());
        verify(transactionRepository, times(1)).findByAccountId(accountId);
    }

    // Test case for getTransactionsByAccountId when no transactions are found
    @Test
    void testGetTransactionsByAccountId_ThrowsAccountNotFoundException() {
        // Arrange
        when(transactionRepository.findByAccountId(accountId)).thenReturn(new ArrayList<>());

        // Act & Assert
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () ->
                transactionService.getTransactionsByAccountId(accountId)
        );
        assertEquals("Account not found with accountId : " + accountId, exception.getMessage());
        verify(transactionRepository, times(1)).findByAccountId(accountId);
    }
}

