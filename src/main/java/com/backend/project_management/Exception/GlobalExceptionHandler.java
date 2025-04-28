package com.backend.project_management.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email, Please enter valid email");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password, Please enter valid password");
    }

    @ExceptionHandler(RequestNotFound.class)
    public ResponseEntity<String> handleRequestNotFoundException(RequestNotFound ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError != null) {
            if (fieldError.getField().equals("name") && 
                fieldError.getDefaultMessage() != null && 
                fieldError.getDefaultMessage().contains("letters and spaces")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Numbers are not allowed in name. Please use only letters and spaces.");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMessage = ex.getMessage();
        if (errorMessage != null && errorMessage.contains("Name must contain only letters and spaces")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Numbers are not allowed in name. Please use only letters and spaces.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
}
