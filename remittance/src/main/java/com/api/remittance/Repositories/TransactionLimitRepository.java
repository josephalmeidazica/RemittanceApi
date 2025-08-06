package com.api.remittance.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.remittance.Entities.TransactionLimit;
import com.api.remittance.Enum.UserType;

public interface TransactionLimitRepository extends JpaRepository<TransactionLimit, Long> {
    Optional<TransactionLimit> findByUserType(UserType userType);

}
