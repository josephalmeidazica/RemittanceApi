package com.api.remittance.Repositories;

import com.api.remittance.Entities.User;
import com.api.remittance.Enum.UserType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should find user by document")
    void testFindByDocument() {
        // Arrange
        User user = new User();
        user.setDocument("12345678900");
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setUserType(UserType.PF);
        user.setPassword("password123");
        userRepository.save(user);

        // Act
        User found = userRepository.findByDocument("12345678900");

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getDocument()).isEqualTo("12345678900");
        assertThat(found.getName()).isEqualTo("Test User");
    }

    @Test
    @DisplayName("Should return null when document does not exist")
    void testFindByDocumentNotFound() {
        User found = userRepository.findByDocument("nonexistent");
        assertThat(found).isNull();
    }
}