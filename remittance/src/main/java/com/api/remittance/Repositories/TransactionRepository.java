package com.api.remittance.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.remittance.Entities.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
