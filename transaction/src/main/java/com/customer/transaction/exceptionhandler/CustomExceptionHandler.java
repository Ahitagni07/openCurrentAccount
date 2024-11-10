package com.customer.transaction.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(AccountNotFoundException.class)
    public Map<String, String> handleCustomerNotFoundException(AccountNotFoundException exception) {
        Map<String, String> map =  new HashMap<>();
        map.put("message", exception.getMessage());
        return map;
    }
}
