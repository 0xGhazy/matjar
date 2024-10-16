package com.matjar.usermanagementapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationResponseError {
    private String field;
    private String message;
}

