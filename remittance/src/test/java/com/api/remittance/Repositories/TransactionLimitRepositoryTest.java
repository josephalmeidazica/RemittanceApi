package com.api.remittance.Repositories;

import com.api.remittance.Entities.TransactionLimit;
import com.api.remittance.Enum.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ContextConfiguration
class TransactionLimitRepositoryTest {

    @Autowired
    private TransactionLimitRepository transactionLimitRepository;

    @Test
    @DisplayName("Should find TransactionLimit by UserType")
    void testFindByUserType() {
        // Arrange
        TransactionLimit limit = new TransactionLimit();
        limit.setUserType(UserType.PF);
        limit.setLimitAmount(1000.0);
        transactionLimitRepository.save(limit);

        // Act
        Optional<TransactionLimit> found = transactionLimitRepository.findByUserType(UserType.PF);

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getUserType()).isEqualTo(UserType.PF);
        assertThat(found.get().getLimitAmount()).isEqualTo(1000.0);
    }

    @Test
    @DisplayName("Should return empty when UserType not found")
    void testFindByUserTypeNotFound() {
        Optional<TransactionLimit> found = transactionLimitRepository.findByUserType(null);
        assertThat(found).isNotPresent();
    }
}