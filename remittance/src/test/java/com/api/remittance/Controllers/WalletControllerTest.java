package com.api.remittance.Controllers;

import com.api.remittance.Entities.User;
import com.api.remittance.Entities.Wallet;
import com.api.remittance.Enum.Currency;
import com.api.remittance.Exceptions.WalletNotFoundException;
import com.api.remittance.Repositories.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletControllerTest {

    private WalletRepository repository;
    private WalletController controller;

    @BeforeEach
    void setUp() {
        repository = mock(WalletRepository.class);
        controller = new WalletController(repository);
    }

    @Test
    void testAll_ReturnsAllWallets() {
        Wallet wallet1 = new Wallet();
        Wallet wallet2 = new Wallet();
        when(repository.findAll()).thenReturn(Arrays.asList(wallet1, wallet2));

        List<Wallet> result = controller.all();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void testNewWallet_SavesAndReturnsWallet() {
        Wallet wallet = new Wallet();
        when(repository.save(wallet)).thenReturn(wallet);

        Wallet result = controller.newWallet(wallet);

        assertEquals(wallet, result);
        verify(repository).save(wallet);
    }

    @Test
    void testOne_ReturnsWallet_WhenFound() {
        Wallet wallet = new Wallet();
        when(repository.findById(1L)).thenReturn(Optional.of(wallet));

        Wallet result = controller.one(1L);

        assertEquals(wallet, result);
        verify(repository).findById(1L);
    }

    @Test
    void testOne_ThrowsException_WhenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> controller.one(1L));
        verify(repository).findById(1L);
    }

    @Test
    void testUpdaWallet_UpdatesExistingWallet() {
        Wallet existing = new Wallet();
        existing.setId(1L);
        existing.setBalance(100.0);
        existing.setUser(new User());
        existing.setCurrency(Currency.USD);

        Wallet update = new Wallet();
        existing.setBalance(200.0);
        existing.setUser(new User());
        existing.setCurrency(Currency.BRL);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        controller.updaWallet(1L, update);

        assertEquals(update.getBalance(), existing.getBalance());
        assertEquals(update.getUser(), existing.getUser());
        assertEquals(update.getCurrency(), existing.getCurrency());
        verify(repository).save(existing);
    }

    @Test
    void testUpdaWallet_CreatesWallet_WhenNotFound() {
        Wallet update = new Wallet();
        update.setBalance(300.0);
        update.setUser(new User());
        update.setCurrency(null);

        when(repository.findById(2L)).thenReturn(Optional.empty());
        when(repository.save(update)).thenReturn(update);

        Wallet result = controller.updaWallet(2L, update);

        assertEquals(update, result);
        assertEquals(2L, update.getId());
        verify(repository).save(update);
    }

    @Test
    void testDeletewallet_DeletesWallet() {
        controller.deletewallet(5L);
        verify(repository).deleteById(5L);
    }
}