package com.customer.account.controller;

import com.customer.account.dto.UserInfoResponse;
import com.customer.account.entity.Account;
import com.customer.account.exceptionhandler.ConnectivityException;
import com.customer.account.exceptionhandler.CustomerNotFoundException;
import com.customer.account.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    @Mock
    private AccountService accountService;

    //@InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOpenAccount_SuccessfulCreation() throws CustomerNotFoundException, ConnectivityException {
        Long customerID = 1L;
        double initialCredit = 100.0;
        Account account = new Account();
        account.setId(123L);

        // Mock the service method
        when(accountService.openAccount(customerID, initialCredit)).thenReturn(Optional.of(account));

        // Call the controller method
        ResponseEntity<String> response = accountController.openAccount(customerID, initialCredit);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Account created successfully with accountId: 123 for customer Id 1", response.getBody());

        verify(accountService, times(1)).openAccount(customerID, initialCredit);
    }

    @Test
    void testOpenAccount_FailedCreation() throws CustomerNotFoundException, ConnectivityException {
        Long customerID = 1L;
        double initialCredit = 100.0;

        // Mock the service method to return empty (account creation failed)
        when(accountService.openAccount(customerID, initialCredit)).thenReturn(Optional.empty());

        // Call the controller method
        ResponseEntity<String> response = accountController.openAccount(customerID, initialCredit);

        // Assertions
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Account could not be created for customer Id 1", response.getBody());

        verify(accountService, times(1)).openAccount(customerID, initialCredit);
    }

//    @Test
//    void testOpenAccount_ExceptionHandling() throws CustomerNotFoundException, ConnectivityException {
//        Long customerID = 1L;
//        double initialCredit = 100.0;
//
//        // Mock the service method to throw an exception
//        when(accountService.openAccount(customerID, initialCredit)).thenThrow(new RuntimeException("Database error"));
//
//        // Verify that the exception is thrown
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            accountController.openAccount(customerID, initialCredit);
//        });
//
//        assertEquals("java.lang.RuntimeException: Database error", exception.getMessage());
//        verify(accountService, times(1)).openAccount(customerID, initialCredit);
//    }

    @Test
    void testGetAccountInfo_SuccessfulFetch() throws CustomerNotFoundException {
        Long customerID = 1L;
        UserInfoResponse userInfoResponse = new UserInfoResponse();

        // Mock the service method
        when(accountService.getAccountInfo(customerID)).thenReturn(Optional.of(userInfoResponse));

        // Call the controller method
        ResponseEntity<UserInfoResponse> response = accountController.getAccountInfo(customerID);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userInfoResponse, response.getBody());

        verify(accountService, times(1)).getAccountInfo(customerID);
    }

    @Test
    void testGetAccountInfo_CustomerNotFound() throws CustomerNotFoundException {
        Long customerID = 1L;

        // Mock the service method to return empty (customer not found)
        when(accountService.getAccountInfo(customerID)).thenReturn(Optional.empty());

        // Verify that CustomerNotFoundException is thrown
        assertThrows(CustomerNotFoundException.class, () -> {
            accountController.getAccountInfo(customerID);
        });

        verify(accountService, times(1)).getAccountInfo(customerID);
    }
}