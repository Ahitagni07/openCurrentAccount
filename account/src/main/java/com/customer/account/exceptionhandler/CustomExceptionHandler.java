package com.customer.account.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CustomerNotFoundException.class)
    public Map<String, String> handleCustomerNotFoundException(CustomerNotFoundException exception) {
        Map<String, String> map =  new HashMap<>();
        map.put("message", exception.getMessage());
        return map;
    }
}
