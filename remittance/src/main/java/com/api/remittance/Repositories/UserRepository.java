package com.api.remittance.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.remittance.Entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByDocument(String document);
}
