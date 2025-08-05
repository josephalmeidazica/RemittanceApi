package com.api.remittance.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.remittance.Entities.User;
import com.api.remittance.Exceptions.UserNotFoundException;
import com.api.remittance.Repositories.UserRepository;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class UserController {
    private final UserRepository repository;

    UserController(UserRepository repository) {
        this.repository = repository;
    }

    // Aggregte root
    // tag::get-aggregate-root[]
    // @GetMapping("/users")
    List<User> all() {
        return repository.findAll();
    }

    // end::get-aggregate-root[]

    @PostMapping("/users")
    User newUser(User newUser) {
        return repository.save(newUser);
    }

    // Single item
    @GetMapping("/users/{id}")
    User one(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @PutMapping("users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User entity) {
        repository.findById(id)
                .map(user -> {
                    user.setName(entity.getName());
                    user.setEmail(entity.getEmail());
                    user.setUserType(entity.getUserType());
                    user.setPassword(entity.getPassword());
                    user.setDocument(entity.getDocument());
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    entity.setId(id);
                    return repository.save(entity);
                });
        
        return entity;
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
    }


    
}
