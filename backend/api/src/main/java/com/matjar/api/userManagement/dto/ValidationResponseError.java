package com.matjar.api.userManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationResponseError {
    private String field;
    private String message;
}

