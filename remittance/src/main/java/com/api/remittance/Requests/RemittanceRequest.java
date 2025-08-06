package com.api.remittance.Requests;

import com.api.remittance.Enum.Currency;

public class RemittanceRequest {
    private Long senderId;
    private Long receiverId;
    private Double amount;
    private Currency currency;

    public RemittanceRequest() {
    }

    public RemittanceRequest(Long senderId, Long receiverId, Double amount, Currency currency) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.currency = currency;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
