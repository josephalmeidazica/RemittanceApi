package com.api.remittance.Entities;

import com.api.remittance.Enum.UserType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TransactionLimit {
    private @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;
    private UserType userType;
    private Double limitAmount;

    public TransactionLimit() {
    }

    public TransactionLimit(UserType userType, Double limitAmount) {
        this.userType = userType;
        this.limitAmount = limitAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Double getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(Double limitAmount) {
        this.limitAmount = limitAmount;
    }
}
