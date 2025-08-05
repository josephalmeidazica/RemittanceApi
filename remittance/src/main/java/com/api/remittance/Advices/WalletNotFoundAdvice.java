package com.api.remittance.Advices;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.api.remittance.Exceptions.WalletNotFoundException;

@RestController
public class WalletNotFoundAdvice {

    @ExceptionHandler(WalletNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String userNotFoundHandler(WalletNotFoundException ex) {
        return ex.getMessage();
    }
}
