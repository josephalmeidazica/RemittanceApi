package com.api.remittance.Exceptions;

import java.time.LocalDate;


public class TransactionOutOfLimitException extends RuntimeException {

    public TransactionOutOfLimitException(Long userId, LocalDate date) {
        super("Transaction limit exceeded for user " + userId + " on date " + date);
    }
}
