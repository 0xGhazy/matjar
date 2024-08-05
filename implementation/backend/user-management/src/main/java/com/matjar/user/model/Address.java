package com.matjar.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Address {
    @Id
    private String id;
    private String userId;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String country;
    private String zipCode;
}
