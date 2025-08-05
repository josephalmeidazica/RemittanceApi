package com.api.remittance.Entities;

import java.util.Objects;

import com.api.remittance.Enum.UserType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    private @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;
    
    private String name;
    
    private String email;
    
    private UserType userType;
    
    private String password;
    
    private String document;

    public User() {
    }

    public User(String name, String email, UserType userType, String password, String document) {
        this.name = name;
        this.email = email;
        this.userType = userType;
        this.password = password;
        this.document = document;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id) &&
               Objects.equals(name, user.name) &&
               Objects.equals(email, user.email) &&
               Objects.equals(userType, user.userType) &&
               Objects.equals(password, user.password) &&
               Objects.equals(document, user.document);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, userType, password, document);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", userType='" + userType + '\'' +
                ", password='" + password + '\'' +
                ", document='" + document + '\'' +
                '}';
    }


}
