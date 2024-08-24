package com.matjar.user.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "accounts", schema = "user_management", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class Account {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    @Column
    private String email;
    private String password;
    private String gender;
    private String phone;
    private Date dateOfBirth;
    private String status;
    private LocalDateTime lastLoginTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
