package com.matjar.user.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DuplicateEmailException extends RuntimeException{

    public DuplicateEmailException(String message) {
        super(message);
    }

}
