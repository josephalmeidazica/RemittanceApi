package com.api.remittance.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.remittance.Enum.Currency;
import com.api.remittance.Entities.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUserIdAndCurrency(Long id, Currency currency);

    Optional<List<Wallet>> findByUserId(Long id);

    
} 