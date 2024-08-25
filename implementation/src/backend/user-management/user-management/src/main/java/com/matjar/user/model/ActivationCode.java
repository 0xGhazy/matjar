package com.matjar.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "activation_code", schema = "user_management")
@Builder
public class ActivationCode {

    @Id
    private String id;
    private String code;
    private String status;
    private String email;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;

}
