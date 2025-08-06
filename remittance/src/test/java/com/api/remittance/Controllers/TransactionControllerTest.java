package com.api.remittance.Controllers;

import com.api.remittance.Entities.Transaction;
import com.api.remittance.Entities.Wallet;
import com.api.remittance.Enum.TransactionStatus;
import com.api.remittance.Exceptions.InvalidTransactionStatusException;
import com.api.remittance.Exceptions.TransactionNotFoundException;
import com.api.remittance.Repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    private TransactionRepository repository;
    private TransactionController controller;

    @BeforeEach
    void setUp() {
        repository = mock(TransactionRepository.class);
        controller = new TransactionController(repository);
    }

    @Test
    void testAllReturnsAllTransactions() {
        Transaction t1 = new Transaction();
        Transaction t2 = new Transaction();
        when(repository.findAll()).thenReturn(Arrays.asList(t1, t2));

        List<Transaction> result = controller.all();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void testNewTransactionSavesAndReturnsTransaction() {
        Transaction newTransaction = new Transaction();
        when(repository.save(newTransaction)).thenReturn(newTransaction);

        Transaction result = controller.newTransaction(newTransaction);

        assertEquals(newTransaction, result);
        verify(repository).save(newTransaction);
    }

    @Test
    void testOneReturnsTransactionIfExists() {
        Transaction t = new Transaction();
        when(repository.findById(1L)).thenReturn(Optional.of(t));

        Transaction result = controller.one(1L);

        assertEquals(t, result);
        verify(repository).findById(1L);
    }

    @Test
    void testOneThrowsIfNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> controller.one(1L));
    }

    @Test
    void testUpdateTransactionUpdatesExisting() {
        Transaction existing = new Transaction();
        existing.setId(1L);
        existing.setAmount(100.0);
        existing.setSender(new Wallet());
        existing.setReceiver(new Wallet());
        existing.setStatus(TransactionStatus.PENDING);

        Transaction update = new Transaction();
        update.setAmount(200.0);
        update.setSender(new Wallet());
        update.setReceiver(new Wallet());
        update.setStatus(TransactionStatus.COMPLETED);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        Transaction result = controller.updateTransaction(1L, update);

        assertEquals(update.getAmount(), existing.getAmount());
        assertEquals(update.getSender(), existing.getSender());
        assertEquals(update.getReceiver(), existing.getReceiver());
        assertEquals(update.getStatus(), existing.getStatus());
        verify(repository).save(existing);
        assertEquals(update, result);
    }

    @Test
    void testUpdateTransactionCreatesIfNotFound() {
        Transaction update = new Transaction();
        update.setAmount(200.0);
        update.setSender(new Wallet());
        update.setReceiver(new Wallet());
        update.setStatus(TransactionStatus.COMPLETED);

        when(repository.findById(1L)).thenReturn(Optional.empty());
        when(repository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        Transaction result = controller.updateTransaction(1L, update);

        assertEquals(1L, update.getId());
        verify(repository).save(update);
        assertEquals(update, result);
    }

    @Test
    void testPutMethodNameCancelsPendingTransaction() {
        Transaction t = new Transaction();
        t.setId(1L);
        t.setStatus(TransactionStatus.PENDING);

        when(repository.findById(1L)).thenReturn(Optional.of(t));
        when(repository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        Transaction result = controller.putMethodName(1L);

        assertEquals(TransactionStatus.CANCELLED, t.getStatus());
        verify(repository).save(t);
        assertEquals(t, result);
    }

    @Test
    void testPutMethodNameThrowsIfNotPending() {
        Transaction t = new Transaction();
        t.setId(1L);
        t.setStatus(TransactionStatus.COMPLETED);

        when(repository.findById(1L)).thenReturn(Optional.of(t));

        assertThrows(InvalidTransactionStatusException.class, () -> controller.putMethodName(1L));
    }

    @Test
    void testPutMethodNameThrowsIfNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> controller.putMethodName(1L));
    }
}