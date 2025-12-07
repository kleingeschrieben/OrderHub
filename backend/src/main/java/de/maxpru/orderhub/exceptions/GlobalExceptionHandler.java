package de.maxpru.orderhub.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderHubException.class)
    public ResponseEntity<?> handleOrderHubExceptionException(OrderHubException ex) {
        return buildErrorResponse(ex.getErrorCode(), ex.getMessage(), ex.getHttpStatus());
    }

    // FBK
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = "Validation failed";

        FieldError error = ex.getBindingResult().getFieldError();
        if (error != null) {
            message = error.getField() + " " + error.getDefaultMessage();
        }

        return buildErrorResponse("VALIDATION_FAILED", message, HttpStatus.BAD_REQUEST);
    }

    // FBK
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        String message = "Validation failed";

        ConstraintViolation<?> violation = ex.getConstraintViolations()
                .stream()
                .findFirst()
                .orElse(null);

        if (violation != null) {
            message = violation.getPropertyPath() + " " + violation.getMessage();
        }

        return buildErrorResponse("VALIDATION_FAILED", message, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String errorCode, String message, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message, status.value(), Instant.now());
        return new ResponseEntity<>(errorResponse, status);
    }

}
