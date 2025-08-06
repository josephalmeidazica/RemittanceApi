package com.api.remittance.Exceptions;

import com.api.remittance.Enum.UserType;

public class LimitNotFoundException extends RuntimeException {
    public LimitNotFoundException(UserType userType) {
        super("Could not find limit for user type " + userType);
    }

}
