package com.api.remittance.Exceptions;

import com.api.remittance.Enum.Currency;

public class InsuficientBalanceException extends RuntimeException {
    public InsuficientBalanceException(Long userId, Long walletId, Currency currency) {
        super("Not enough balance in wallet" + walletId + " for user " + userId + " in currency " + currency);
    }

}
