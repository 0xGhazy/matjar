package com.matjar.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountActivationRequest {
    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Activation code must not be empty")
    @Size(min = 6, max = 6, message = "Activation code must be 6 characters long")
    private String code;
}
