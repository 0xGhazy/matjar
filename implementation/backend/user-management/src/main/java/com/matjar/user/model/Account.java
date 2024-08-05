package com.matjar.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Account {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private AccountStatus status;
    private LocalDateTime lastLoginTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
