package com.api.remittance.Entities;

import java.util.Objects;

import com.api.remittance.Enum.Currency;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "wallets")
public class Wallet {
    private @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id 
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    
    private Currency currency;
    
    private Double balance;

    public Wallet() {
    }

    public Wallet(User user, Currency currency, Double balance) {
        this.user = user;
        this.currency = currency;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wallet)) return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(id, wallet.id) &&
               Objects.equals(user, wallet.user) &&
               currency == wallet.currency &&
               Objects.equals(balance, wallet.balance);
    }

    @Override
    public int hashCode() { 
        return Objects.hash(id, user, currency, balance);
    }

    @Override
    public String toString() {
        return "Wallet{" +
               "id=" + id +
               ", user=" + user +
               ", currency=" + currency +
               ", balance=" + balance +
               '}';
    }
}
