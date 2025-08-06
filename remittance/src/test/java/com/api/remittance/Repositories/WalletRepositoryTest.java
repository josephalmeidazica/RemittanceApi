package com.api.remittance.Repositories;

import com.api.remittance.Entities.User;
import com.api.remittance.Entities.Wallet;
import com.api.remittance.Enum.Currency;
import com.api.remittance.Enum.UserType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;




@DataJpaTest
class WalletRepositoryTest {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("Joseph","josephalmeidazica@gmail.com",UserType.PF,"123456","12345678900");
        userRepository.save(user);
    }

    @Test
    @DisplayName("findByUserIdAndCurrency returns wallet when exists")
    void testFindByUserIdAndCurrency() {
        Wallet wallet = new Wallet(user, Currency.USD, 0.0);
        walletRepository.save(wallet);

        Optional<Wallet> found = walletRepository.findByUserIdAndCurrency(user.getId(), Currency.USD);

        assertThat(found).isPresent();
        assertThat(found.get().getUser().getId()).isEqualTo(user.getId());
        assertThat(found.get().getCurrency()).isEqualTo(Currency.USD);
    }

    @Test
    @DisplayName("findByUserIdAndCurrency returns empty when not exists")
    void testFindByUserIdAndCurrencyNotFound() {
        Optional<Wallet> found = walletRepository.findByUserIdAndCurrency(99L, Currency.BRL);
        assertThat(found).isNotPresent();
    }

    @Test
    @DisplayName("findByUserId returns list of wallets for user")
    void testFindByUserId() {
        Wallet wallet1 = new Wallet();
        wallet1.setUser(user);
        wallet1.setCurrency(Currency.USD);
        walletRepository.save(wallet1);

        Wallet wallet2 = new Wallet();
        wallet2.setUser(user);
        wallet2.setCurrency(Currency.BRL);
        walletRepository.save(wallet2);

        Optional<List<Wallet>> wallets = walletRepository.findByUserId(user.getId());

        assertThat(wallets).isPresent();
        assertThat(wallets.get()).hasSize(2);
    }

    @Test
    @DisplayName("findByUserId returns empty list when user has no wallets")
    void testFindByUserIdEmpty() {
        Optional<List<Wallet>> wallets = walletRepository.findByUserId(123L);
        assertThat(wallets).isPresent();
        assertThat(wallets.get()).isEmpty();
    }


}