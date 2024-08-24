package com.matjar.user.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

import lombok.Builder;

@Builder
@Data
public class Response {
    private String message;
    private HttpStatus status;
    private Object data;
}
