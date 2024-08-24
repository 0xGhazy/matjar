package com.matjar.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidActivationCodeException extends RuntimeException {

    public InvalidActivationCodeException(String message) {
        super(message);
    }

}
