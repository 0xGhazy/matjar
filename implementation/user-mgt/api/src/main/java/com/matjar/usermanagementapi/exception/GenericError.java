package com.matjar.usermanagementapi.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@NoArgsConstructor
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class GenericError extends RuntimeException {

    private final String reason = "System Internal Failure";
    private final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    public GenericError(String message) {
        super(message);
    }
}
