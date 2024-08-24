package com.matjar.user.exception;

import com.matjar.user.dto.ResponseError;
import com.matjar.user.dto.response.ValidationError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ArrayList<ValidationError> errors = new ArrayList<>();

        ex
                .getBindingResult()
                .getAllErrors()
                .forEach( (error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.add(new ValidationError(fieldName, errorMessage));
                });

        ResponseError error = ResponseError.builder()
                .timestamp(new Date())
                .details(errors)
                .message("validation error")
                .requestUrl(request.getDescription(false))
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ResponseError> handleDuplicateEmailException(DuplicateEmailException exception, WebRequest webRequest) {
        ResponseError error = ResponseError.builder()
                .message(exception.getMessage())
                .timestamp(new Date())
                .requestUrl(webRequest.getDescription(false))
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResponseError> handleValidationException(ValidationException exception, WebRequest webRequest) {
        ResponseError error = ResponseError.builder()
                .message(exception.getMessage())
                .timestamp(new Date())
                .requestUrl(webRequest.getDescription(false))
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
