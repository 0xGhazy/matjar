package com.matjar.api.userManagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivateUserRequest {
    @NotBlank(message = "cannot be empty")
    @Email
    private String email;
    @NotBlank(message = "cannot be empty")
    private String code;
}



