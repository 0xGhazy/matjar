package com.matjar.usermanagementapi.exception;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@NoArgsConstructor
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequest extends RuntimeException{

    private final String reason = "Missing or invalid parameter or value";
    private final HttpStatus status = HttpStatus.BAD_REQUEST;

    public BadRequest(String message) {
        super(message);
    }

}
