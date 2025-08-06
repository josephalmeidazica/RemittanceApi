package com.api.remittance.Repositories;

import com.api.remittance.Entities.Transaction;
import com.api.remittance.Entities.User;
import com.api.remittance.Entities.Wallet;
import com.api.remittance.Enum.Currency;
import com.api.remittance.Enum.TransactionStatus;
import com.api.remittance.Enum.UserType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;




@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    private Transaction transaction1;
    private Transaction transaction2;

    private User user1;
    private User user2;
    private Wallet wallet1;
    private Wallet wallet2;

    @BeforeEach
    public void setUp() {
        user1 = new User("Joseph","josephalmeidazica@gmail.com",UserType.PF,"123456","12345678900");
        user2 = new User("Gabriel","gabriel.silva@gmail.com",UserType.PF,"123456","12345678900");
        userRepository.save(user1);
        userRepository.save(user2);

        wallet1 = new Wallet();
        wallet1.setUser(user1);
        wallet1.setCurrency(Currency.USD);

        wallet2 = new Wallet();
        wallet2.setUser(user2);
        wallet2.setCurrency(Currency.BRL);

        walletRepository.save(wallet1);
        walletRepository.save(wallet2);

        transaction1 = new Transaction();
        transaction1.setSender(wallet1);
        transaction1.setStatus(TransactionStatus.PENDING);
        transaction1.setCreatedAt(LocalDate.now());

        transaction2 = new Transaction();
        transaction2.setSender(wallet2);
        transaction2.setStatus(TransactionStatus.COMPLETED);
        transaction2.setCreatedAt(LocalDate.now());

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
    }

    @Test
    @DisplayName("Should find transactions by senderId and status")
    void testFindBySenderIdAndStatus() {
        Optional<List<Transaction>> result = transactionRepository.findBySenderIdAndStatus(wallet1.getId(), TransactionStatus.PENDING);
        assertThat(result).isPresent();
        assertThat(result.get()).hasSize(1);
        assertThat(result.get().get(0).getStatus()).isEqualTo(TransactionStatus.PENDING);
    }

    @Test
    @DisplayName("Should find transactions by senderId, status, and createdAt")
    void testFindBySenderIdAndStatusAndCreatedAt() {
        LocalDate today = LocalDate.now();
        Optional<List<Transaction>> result = transactionRepository.findBySenderIdAndStatusAndCreatedAt(wallet1.getId(), TransactionStatus.PENDING, today);
        assertThat(result).isPresent();
        assertThat(result.get()).hasSize(1);
        assertThat(result.get().get(0).getCreatedAt()).isEqualTo(today);
    }

    @Test
    @DisplayName("Should return empty when no transactions match criteria")
    void testFindBySenderIdAndStatusAndCreatedAt_NoMatch() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Optional<List<Transaction>> result = transactionRepository.findBySenderIdAndStatusAndCreatedAt(1L, TransactionStatus.PENDING, yesterday);
        assertThat(result).isPresent();
        assertThat(result.get()).isEmpty();
    }
}