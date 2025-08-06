package com.api.remittance.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.remittance.Entities.Wallet;
import com.api.remittance.Exceptions.WalletNotFoundException;
import com.api.remittance.Repositories.WalletRepository;

@RestController
public class WalletController {
    private final WalletRepository repository;

    WalletController(WalletRepository repository) {
        this.repository = repository;
    }

    // Aggregte root
    // tag::get-aggregate-root[]
    // @GetMapping("/users")
    List<Wallet> all() {
        return repository.findAll();
    }

    // end::get-aggregate-root[]

        @PostMapping("/wallets")
    Wallet newWallet(Wallet newWallet) {
        return repository.save(newWallet);
    }

    // Single item
    @GetMapping("/wallets/{id}")
    Wallet one(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new WalletNotFoundException(id));
    }

    @PutMapping("wallets/{id}")
    public Wallet updaWallet(@PathVariable Long id, @RequestBody Wallet entity) {
        repository.findById(id)
                .map(wallet -> {
                    wallet.setBalance(entity.getBalance());
                    wallet.setUser(entity.getUser());
                    wallet.setCurrency(entity.getCurrency());
                    return repository.save(wallet);
                })
                .orElseGet(() -> {
                    entity.setId(id);
                    return repository.save(entity);
                });
        
        return entity;
    }

    @DeleteMapping("/wallets/{id}")
    void deletewallet(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
