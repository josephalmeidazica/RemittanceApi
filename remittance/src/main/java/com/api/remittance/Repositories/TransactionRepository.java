package com.api.remittance.Repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.remittance.Entities.Transaction;
import com.api.remittance.Enum.TransactionStatus;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<List<Transaction>> findBySenderIdAndStatus(Long senderId, TransactionStatus status);

    Optional<List<Transaction>> findBySenderIdAndStatusAndCreatedAt(Long senderId, TransactionStatus status, LocalDate date);

}
