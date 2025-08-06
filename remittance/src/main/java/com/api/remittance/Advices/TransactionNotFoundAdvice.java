package com.api.remittance.Advices;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.api.remittance.Exceptions.TransactionNotFoundException;

@RestController
public class TransactionNotFoundAdvice {

    @ExceptionHandler(TransactionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String userNotFoundHandler(TransactionNotFoundException ex) {
        return ex.getMessage();
    }
}
