package com.api.remittance.Exceptions;

import com.api.remittance.Enum.Currency;

public class WalletNotFoundForUserAndCurrencyException extends RuntimeException {
    public WalletNotFoundForUserAndCurrencyException(Long id, Currency currency) {
        super("Could not find wallet for user " + id + " and currency " + currency);
    }

}
