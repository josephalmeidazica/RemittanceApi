package com.api.remittance.Entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.api.remittance.Enum.Currency;
import com.api.remittance.Enum.TransactionStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {
    private @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id 
    Long id;

    @OneToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private Wallet sender;

    @OneToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    private Wallet receiver;

    private Double amount;

    private Currency currency;

    private TransactionStatus status;

    private LocalDate createdAt;

    private LocalDateTime updatedAt;
    
    public Transaction() {
    }

    public Transaction(Wallet sender, Wallet receiver, Double amount, Currency currency) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.currency = currency;
        this.status = TransactionStatus.PENDING; // Default status when a transaction is created
        this.createdAt = LocalDate.now(); // Set creation date to now
        this.updatedAt = LocalDateTime.now(); // Set updated date to now
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Wallet getSender() {
        return sender;
    }

    public void setSender(Wallet sender) {
        this.sender = sender;
    }

    public Wallet getReceiver() {
        return receiver;
    }

    public void setReceiver(Wallet receiver) {
        this.receiver = receiver;
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

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;

        Transaction that = (Transaction) o;

        if (!id.equals(that.id)) return false;
        if (!sender.equals(that.sender)) return false;
        if (!receiver.equals(that.receiver)) return false;
        if (!amount.equals(that.amount)) return false;
        if (status != that.status) return false;
        return currency == that.currency;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + sender.hashCode();
        result = 31 * result + receiver.hashCode();
        result = 31 * result + amount.hashCode();
        result = 31 * result + currency.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", amount=" + amount +
                ", currency=" + currency +
                ", status=" + status +
                '}';
    }
}
