package com.api.remittance.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.remittance.Repositories.TransactionLimitRepository;
import com.api.remittance.Entities.TransactionLimit;
import com.api.remittance.Exceptions.TransactionNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class TransactionLimitController {
    private final TransactionLimitRepository repository;

    public TransactionLimitController(TransactionLimitRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/transaction-limits")
    public List<TransactionLimit> getAllTransactionLimits() {
        return repository.findAll();
    }

    @GetMapping("/transaction-limits/{id}")
    public TransactionLimit getTransactionLimitById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    @PostMapping("/transaction-limits")
    public TransactionLimit createTransactionLimit(@RequestBody TransactionLimit transactionLimit) {
        return repository.save(transactionLimit);
    }    
}
