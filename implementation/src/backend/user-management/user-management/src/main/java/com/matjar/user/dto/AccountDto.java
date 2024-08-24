package com.matjar.user.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Valid
@Data
public class AccountDto {

    private String id;

    @NotEmpty(message = "must be not empty")
    @NotNull(message = "must be not nul")
    private String firstName;

    @NotEmpty(message = "must be not empty")
    @NotNull(message = "must be not nul")
    private String lastName;

    @NotEmpty(message = "must be not empty")
    @NotNull(message = "must be not nul")
    @Email
    private String email;

    @NotEmpty(message = "must be not empty")
    @NotNull(message = "must be not nul")
    @Size(min = 8, max = 100, message = "password length should be between 8 and 100 alphanumeric character")
    private String password;

    @NotEmpty(message = "must be not empty")
    @NotNull(message = "must be not nul")
    @Pattern(regexp = "MALE|FEMALE")
    private String gender;

    @NotEmpty(message = "must be not empty")
    @NotNull(message = "must be not nul")
    private String phone;

    @NotNull(message = "must be not nul")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", message = "dob must be on yyyy-MM-dd format")
    private String dob;

    @Override
    public String toString() {
        return "AccountDto{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='***** Masked *****" + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", dob='" + dob + '\'' +
                '}';
    }
}
