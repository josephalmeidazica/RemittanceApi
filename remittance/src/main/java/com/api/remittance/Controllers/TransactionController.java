package com.api.remittance.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.remittance.Entities.Transaction;
import com.api.remittance.Exceptions.TransactionNotFoundException;
import com.api.remittance.Repositories.TransactionRepository;

@RestController
public class TransactionController {
    private final TransactionRepository repository;

    TransactionController(TransactionRepository repository) {
        this.repository = repository;
    }

    // Aggregte root
    // tag::get-aggregate-root[]
    // @GetMapping("/users")
    List<Transaction> all() {
        return repository.findAll();
    }

    // end::get-aggregate-root[]

    @PostMapping("/transactions")
    Transaction newTransaction(Transaction newTransaction) {
        return repository.save(newTransaction);
    }

    // Single item
    @GetMapping("/transactions/{id}")
    Transaction one(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    @PutMapping("transactions/{id}")
    public Transaction updateTransaction(@PathVariable Long id, @RequestBody Transaction entity) {
        repository.findById(id)
                .map(transaction -> {
                    transaction.setAmount(entity.getAmount());
                    transaction.setSender(entity.getSender());
                    transaction.setReceiver(entity.getReceiver());
                    transaction.setStatus(entity.getStatus());
                    return repository.save(transaction);
                })
                .orElseGet(() -> {
                    entity.setId(id);
                    return repository.save(entity);
                });
        
        return entity;
    }
}
