package com.matjar.api.exceptionHandler;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@NoArgsConstructor
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class Ineligible extends RuntimeException {
    private final String reason = "Request failed due to ineligibility";
    private final HttpStatus status = HttpStatus.BAD_REQUEST;
    public Ineligible(String message) {
        super(message);
    }
}
