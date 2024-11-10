package com.customer.transaction.exceptionhandler;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String message) {
        super(message);
    }
    public AccountNotFoundException() {

    }
}
