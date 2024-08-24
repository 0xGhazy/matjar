package com.matjar.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;

@Data
@AllArgsConstructor
@Builder
public class ResponseError {
    private Date timestamp;
    private String message;
    private Object details;
    private Object requestUrl;
}