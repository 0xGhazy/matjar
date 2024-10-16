package com.matjar.usermanagementapi.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    @NotBlank(message = "cannot be empty")
    @Pattern(regexp = "^[a-zA-Z ]+$", message="cannot contains numbers or special chars")
    @Size(max = 20)
    private String firstName;

    @Pattern(regexp = "^[a-zA-Z ]+$", message="cannot contains numbers or special chars")
    @NotBlank(message = "cannot be empty")
    @Size(max = 20)
    private String lastName;

    @NotBlank(message = "cannot be empty")
    @Email
    private String email;

    @NotBlank(message = "cannot be empty")
    private String password;

    @NotBlank(message = "cannot be empty")
    @Pattern(regexp = "^(011|012|015|010)\\d{8}$", message="invalid phone number")
    private String phone;

    @NotBlank(message = "cannot be empty")
    @Pattern(regexp = "MALE|FEMALE", message="unsupported gender")
    private String gender;

    @NotNull(message = "cannot be empty")
    @Past(message = "must be in the past")
    private LocalDate dateOfBirth;

    @Override
    public String toString() {
        return "UserDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password= *** MASKED ***" +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }

}
