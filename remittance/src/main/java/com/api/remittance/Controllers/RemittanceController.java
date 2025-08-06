package com.api.remittance.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.api.remittance.Entities.Transaction;
import com.api.remittance.Entities.User;
import com.api.remittance.Entities.Wallet;
import com.api.remittance.Enum.Currency;
import com.api.remittance.Enum.TransactionStatus;
import com.api.remittance.Exceptions.InsuficientBalanceException;
import com.api.remittance.Exceptions.InvalidTransactionStatusException;
import com.api.remittance.Exceptions.TransactionNotFoundException;
import com.api.remittance.Exceptions.UserNotFoundException;
import com.api.remittance.Exceptions.WalletNotFoundForUserAndCurrencyException;
import com.api.remittance.Repositories.TransactionRepository;
import com.api.remittance.Repositories.UserRepository;
import com.api.remittance.Repositories.WalletRepository;
import com.api.remittance.Requests.RemittanceRequest;
import com.api.remittance.Services.PriceService;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
public class RemittanceController {
    private final PriceService priceService;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public RemittanceController(PriceService priceService, 
                                WalletRepository walletRepository,
                                TransactionRepository transactionRepository,
                                UserRepository userRepository) {
        this.priceService = priceService;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("remittance")
    public Transaction createRemittanceTransaction(@RequestBody RemittanceRequest entity) {
        User sender = userRepository.findById(entity.getSenderId())
                .orElseThrow(() -> new UserNotFoundException(entity.getSenderId()));
        
        User receiver = userRepository.findById(entity.getReceiverId())
                .orElseThrow(() -> new UserNotFoundException(entity.getReceiverId()));
        
        Wallet senderWallet = walletRepository.findByUserIdAndCurrency(sender.getId(), entity.getCurrency())
                .orElseThrow(() -> new WalletNotFoundForUserAndCurrencyException(sender.getId(), entity.getCurrency()));

        Wallet receiverWallet = walletRepository.findByUserIdAndCurrency(receiver.getId(), getReceiverCurrency(entity.getCurrency()))
                .orElseThrow(() -> new WalletNotFoundForUserAndCurrencyException(receiver.getId(), entity.getCurrency()));
        
        // Create transaction first status PROCESSING
        Transaction transaction = transactionRepository.save(new Transaction(senderWallet, receiverWallet, entity.getAmount(), entity.getCurrency()));

        //Check if sender has enough balance
        if (senderWallet.getBalance() < entity.getAmount()) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            throw new InsuficientBalanceException(sender.getId(), senderWallet.getId(), entity.getCurrency());
        }

        // Calculate the price based on the current exchange rate
        Double price = priceService.getPrice();
        Double correctedAmount = 0.0;
        // Send BRL to USD wallet
        if (entity.getCurrency().equals(Currency.BRL)) {
            correctedAmount = entity.getAmount() / price;
        }
        // Send USD to BRL wallet
        else if (entity.getCurrency().equals(Currency.USD)) {
            correctedAmount = entity.getAmount() * price;
        }

        // Update sender and receiver wallets
        senderWallet.setBalance(senderWallet.getBalance() - entity.getAmount());
        receiverWallet.setBalance(correctedAmount + entity.getAmount());
        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);
        // Update transaction status to SUCCESS
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setUpdatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);
        
        return transaction;
    }

    @PostMapping("remittance/revert/{id}")
    public Transaction postMethodName(@RequestHeader Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
        
        if (transaction.getStatus() != TransactionStatus.COMPLETED) {
            throw new InvalidTransactionStatusException(id, transaction.getStatus());
        }

        Wallet senderWallet = transaction.getSender();
        Wallet receiverWallet = transaction.getReceiver();

        if(receiverWallet.getBalance() < transaction.getAmount()) {
            throw new InsuficientBalanceException(receiverWallet.getUser().getId(), receiverWallet.getId(), receiverWallet.getCurrency());
        }

        // Revert the transaction
        senderWallet.setBalance(senderWallet.getBalance() + transaction.getAmount());
        receiverWallet.setBalance(receiverWallet.getBalance() - transaction.getAmount());

        // Save the updated wallets
        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        // Update transaction status to REVERTED
        transaction.setStatus(TransactionStatus.REVERTED);
        transaction.setUpdatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);
        
        return transaction;
    }
    

    private Currency getReceiverCurrency(Currency senderCurrency) {
        return senderCurrency == Currency.BRL ? Currency.USD : Currency.BRL;
    }
    
}
