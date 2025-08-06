package com.api.remittance.Controllers;

import com.api.remittance.Entities.Transaction;
import com.api.remittance.Entities.TransactionLimit;
import com.api.remittance.Entities.User;
import com.api.remittance.Entities.Wallet;
import com.api.remittance.Enum.Currency;
import com.api.remittance.Enum.TransactionStatus;
import com.api.remittance.Enum.UserType;
import com.api.remittance.Exceptions.*;
import com.api.remittance.Repositories.TransactionLimitRepository;
import com.api.remittance.Repositories.TransactionRepository;
import com.api.remittance.Repositories.UserRepository;
import com.api.remittance.Repositories.WalletRepository;
import com.api.remittance.Requests.RemittanceRequest;
import com.api.remittance.Services.PriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RemittanceControllerTest {

    private PriceService priceService;
    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;
    private UserRepository userRepository;
    private TransactionLimitRepository transactionLimitRepository;
    private RemittanceController controller;

    @BeforeEach
    void setUp() {
        priceService = mock(PriceService.class);
        walletRepository = mock(WalletRepository.class);
        transactionRepository = mock(TransactionRepository.class);
        userRepository = mock(UserRepository.class);
        transactionLimitRepository = mock(TransactionLimitRepository.class);
        controller = new RemittanceController(priceService, walletRepository, transactionRepository, userRepository, transactionLimitRepository);
    }

    @Test
    void testCreateRemittanceTransaction_Success_BRLtoUSD() {
        RemittanceRequest request = new RemittanceRequest();
        request.setSenderId(1L);
        request.setReceiverId(2L);
        request.setAmount(100.0);
        request.setCurrency(Currency.BRL);

        User sender = new User();
        sender.setId(1L);
        sender.setUserType(UserType.PF);

        User receiver = new User();
        receiver.setId(2L);

        Wallet senderWallet = new Wallet();
        senderWallet.setId(10L);
        senderWallet.setUser(sender);
        senderWallet.setCurrency(Currency.BRL);
        senderWallet.setBalance(200.0);

        Wallet receiverWallet = new Wallet();
        receiverWallet.setId(20L);
        receiverWallet.setUser(receiver);
        receiverWallet.setCurrency(Currency.USD);
        receiverWallet.setBalance(50.0);

        Transaction transaction = new Transaction(senderWallet, receiverWallet, 100.0, Currency.BRL);
        transaction.setId(100L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(walletRepository.findByUserIdAndCurrency(1L, Currency.BRL)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findByUserIdAndCurrency(2L, Currency.USD)).thenReturn(Optional.of(receiverWallet));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(priceService.getPrice()).thenReturn(5.0);

        List<Wallet> senderWallets = Arrays.asList(senderWallet);
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(senderWallets));
        List<Transaction> completedTransactions = new ArrayList<>();
        when(transactionRepository.findBySenderIdAndStatusAndCreatedAt(eq(10L), eq(TransactionStatus.COMPLETED), any(LocalDate.class)))
                .thenReturn(Optional.of(completedTransactions));
        TransactionLimit limit = new TransactionLimit();
        limit.setLimitAmount(1000.0);
        when(transactionLimitRepository.findByUserType(UserType.PF)).thenReturn(Optional.of(limit));

        Transaction result = controller.createRemittanceTransaction(request);

        assertEquals(TransactionStatus.COMPLETED, result.getStatus());
        assertEquals(100.0, senderWallet.getBalance());
        assertEquals(120.0, receiverWallet.getBalance()); // 50 + (100/5)
        verify(walletRepository, times(1)).save(senderWallet);
        verify(walletRepository, times(1)).save(receiverWallet);
        verify(transactionRepository, atLeastOnce()).save(any(Transaction.class));
    }

    @Test
    void testCreateRemittanceTransaction_InsufficientBalance() {
        RemittanceRequest request = new RemittanceRequest();
        request.setSenderId(1L);
        request.setReceiverId(2L);
        request.setAmount(300.0);
        request.setCurrency(Currency.BRL);

        User sender = new User();
        sender.setId(1L);
        sender.setUserType(UserType.PF);

        User receiver = new User();
        receiver.setId(2L);

        Wallet senderWallet = new Wallet();
        senderWallet.setId(10L);
        senderWallet.setUser(sender);
        senderWallet.setCurrency(Currency.BRL);
        senderWallet.setBalance(100.0);

        Wallet receiverWallet = new Wallet();
        receiverWallet.setId(20L);
        receiverWallet.setUser(receiver);
        receiverWallet.setCurrency(Currency.USD);
        receiverWallet.setBalance(50.0);

        Transaction transaction = new Transaction(senderWallet, receiverWallet, 300.0, Currency.BRL);
        transaction.setId(100L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(walletRepository.findByUserIdAndCurrency(1L, Currency.BRL)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findByUserIdAndCurrency(2L, Currency.USD)).thenReturn(Optional.of(receiverWallet));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        assertThrows(InsuficientBalanceException.class, () -> controller.createRemittanceTransaction(request));
        verify(transactionRepository, atLeastOnce()).save(any(Transaction.class));
    }

    @Test
    void testCreateRemittanceTransaction_OverLimit() {
        RemittanceRequest request = new RemittanceRequest();
        request.setSenderId(1L);
        request.setReceiverId(2L);
        request.setAmount(100.0);
        request.setCurrency(Currency.BRL);

        User sender = new User();
        sender.setId(1L);
        sender.setUserType(UserType.PF);

        User receiver = new User();
        receiver.setId(2L);

        Wallet senderWallet = new Wallet();
        senderWallet.setId(10L);
        senderWallet.setUser(sender);
        senderWallet.setCurrency(Currency.BRL);
        senderWallet.setBalance(200.0);

        Wallet receiverWallet = new Wallet();
        receiverWallet.setId(20L);
        receiverWallet.setUser(receiver);
        receiverWallet.setCurrency(Currency.USD);
        receiverWallet.setBalance(50.0);

        Transaction transaction = new Transaction(senderWallet, receiverWallet, 100.0, Currency.BRL);
        transaction.setId(100L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(walletRepository.findByUserIdAndCurrency(1L, Currency.BRL)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findByUserIdAndCurrency(2L, Currency.USD)).thenReturn(Optional.of(receiverWallet));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(priceService.getPrice()).thenReturn(5.0);

        List<Wallet> senderWallets = Arrays.asList(senderWallet);
        when(walletRepository.findByUserId(1L)).thenReturn(Optional.of(senderWallets));
        List<Transaction> completedTransactions = new ArrayList<>();
        Transaction t = new Transaction(senderWallet, receiverWallet, 1000.0, Currency.BRL);
        completedTransactions.add(t);
        when(transactionRepository.findBySenderIdAndStatusAndCreatedAt(eq(10L), eq(TransactionStatus.COMPLETED), any(LocalDate.class)))
                .thenReturn(Optional.of(completedTransactions));
        TransactionLimit limit = new TransactionLimit();
        limit.setLimitAmount(500.0);
        when(transactionLimitRepository.findByUserType(UserType.PF)).thenReturn(Optional.of(limit));

        assertThrows(LimitNotFoundException.class, () -> controller.createRemittanceTransaction(request));
        verify(transactionRepository, atLeastOnce()).save(any(Transaction.class));
    }

    @Test
    void testPostMethodName_RevertSuccess() {
        Wallet senderWallet = new Wallet();
        senderWallet.setId(1L);
        senderWallet.setBalance(100.0);

        Wallet receiverWallet = new Wallet();
        receiverWallet.setId(2L);
        receiverWallet.setBalance(200.0);

        Transaction transaction = new Transaction(senderWallet, receiverWallet, 50.0, Currency.BRL);
        transaction.setId(10L);
        transaction.setStatus(TransactionStatus.COMPLETED);

        when(transactionRepository.findById(10L)).thenReturn(Optional.of(transaction));
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = controller.postMethodName(10L);

        assertEquals(TransactionStatus.REVERTED, result.getStatus());
        assertEquals(150.0, senderWallet.getBalance());
        assertEquals(150.0, receiverWallet.getBalance());
        verify(walletRepository, times(1)).save(senderWallet);
        verify(walletRepository, times(1)).save(receiverWallet);
        verify(transactionRepository, atLeastOnce()).save(any(Transaction.class));
    }

    @Test
    void testPostMethodName_RevertFails_InvalidStatus() {
        Transaction transaction = mock(Transaction.class);
        when(transaction.getStatus()).thenReturn(TransactionStatus.FAILED);
        when(transactionRepository.findById(10L)).thenReturn(Optional.of(transaction));

        assertThrows(InvalidTransactionStatusException.class, () -> controller.postMethodName(10L));
    }

    @Test
    void testPostMethodName_RevertFails_InsufficientReceiverBalance() {
        Wallet senderWallet = new Wallet();
        senderWallet.setId(1L);
        senderWallet.setBalance(100.0);

        Wallet receiverWallet = new Wallet();
        receiverWallet.setId(2L);
        receiverWallet.setBalance(10.0);
        User receiverUser = new User();
        receiverUser.setId(2L);
        receiverWallet.setUser(receiverUser);

        Transaction transaction = new Transaction(senderWallet, receiverWallet, 50.0, Currency.BRL);
        transaction.setId(10L);
        transaction.setStatus(TransactionStatus.COMPLETED);

        when(transactionRepository.findById(10L)).thenReturn(Optional.of(transaction));

        assertThrows(InsuficientBalanceException.class, () -> controller.postMethodName(10L));
    }
}