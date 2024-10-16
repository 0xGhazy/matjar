package com.matjar.usermanagementapi.exception;

import com.matjar.usermanagementapi.dto.ResponseError;
import com.matjar.usermanagementapi.dto.ValidationResponseError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
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

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {


    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ArrayList<ValidationResponseError> errors = new ArrayList<>();

        ex
                .getBindingResult()
                .getAllErrors()
                .forEach( (error) -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.add(new ValidationResponseError(fieldName, errorMessage));
                });

        ResponseError error = ResponseError.builder()
                .reason("Missing or invalid parameter or value")
                .details(errors)
                .build();
        log.error("reason={}, details={}, message={}", error.getReason(), error.getDetails(), "Validation error");
        return new ResponseEntity<>(error.getResponse(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({ GenericError.class })
    public ResponseEntity<?> handleGenericErrorException(GenericError ex, WebRequest webRequest) {
        MDC.get("trxId");
        ResponseError response = ResponseError.builder()
                .message(ex.getMessage())
                .reason(ex.getReason())
                .build();
        log.error("reason={}, details={}, message={}", response.getReason(), response.getDetails(), response.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(response.getResponse());
    }

    @ExceptionHandler({ InternalSystemError.class })
    public ResponseEntity<?> handleInternalSystemErrorException(InternalSystemError ex, WebRequest webRequest) {
        MDC.get("trxId");
        ResponseError response = ResponseError.builder()
                .message(ex.getMessage())
                .reason(ex.getReason())
                .build();
        log.error("reason={}, details={}, message={}", response.getReason(), response.getDetails(), response.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(response.getResponse());
    }

    @ExceptionHandler({ BadRequest.class })
    public ResponseEntity<?> handleBadRequestException(BadRequest ex, WebRequest webRequest) {
        MDC.get("trxId");
        ResponseError response = ResponseError.builder()
                .message(ex.getMessage())
                .reason(ex.getReason())
                .build();
        log.error("reason={}, details={}, message={}", response.getReason(), response.getDetails(), response.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(response.getResponse());
    }

    @ExceptionHandler({ Ineligible.class })
    public ResponseEntity<?> handleIneligibleException(Ineligible ex, WebRequest webRequest) {
        MDC.get("trxId");
        ResponseError response = ResponseError.builder()
                .message(ex.getMessage())
                .reason(ex.getReason())
                .build();
        log.error("reason={}, details={}, message={}", response.getReason(), response.getDetails(), response.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(response.getResponse());
    }

    @ExceptionHandler({ NotFound.class })
    public ResponseEntity<?> handleNotFoundException(NotFound ex, WebRequest webRequest) {
        MDC.get("trxId");
        ResponseError response = ResponseError.builder()
                .message(ex.getMessage())
                .reason(ex.getReason())
                .build();
        log.error("reason={}, details={}, message={}", response.getReason(), response.getDetails(), response.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(response.getResponse());
    }

}
