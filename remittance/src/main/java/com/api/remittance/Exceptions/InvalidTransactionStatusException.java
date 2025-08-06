package com.api.remittance.Exceptions;

import com.api.remittance.Enum.TransactionStatus;

public class InvalidTransactionStatusException extends RuntimeException {
    public InvalidTransactionStatusException(Long id, TransactionStatus status) {
        super("Transaction " + id + "has the status" + status + ", cannot be cancelled.");
    }

}
