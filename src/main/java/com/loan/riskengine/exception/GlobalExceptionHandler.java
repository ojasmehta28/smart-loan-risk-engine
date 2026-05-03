package com.loan.riskengine.exception;

// I implemented centralized exception handling using @RestControllerAdvice and @ExceptionHandler to provide structured validation error responses 
import org.springframework.web.bind.annotation.*; 
import org.springframework.http.ResponseEntity; 
import org.springframework.http.HttpStatus; 
import org.springframework.web.bind.MethodArgumentNotValidException; 

import java.util.HashMap; 
import java.util.Map; 

@RestControllerAdvice 
public class GlobalExceptionHandler {

    // =========================================================
    // EXISTING: Validation Error Handler (@Valid)
    // =========================================================
    @ExceptionHandler(MethodArgumentNotValidException.class) 
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {  

        Map<String, String> errors = new HashMap<>(); 

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Validation failed"); 
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    // =========================================================
    // NEW: Handle RuntimeException (YOUR RULE VALIDATION)
    // =========================================================
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {

        Map<String, Object> response = new HashMap<>();

        response.put("message", "Error");
        response.put("details", ex.getMessage());

        // Example:
        // "Expression cannot be empty"
        // "Invalid operator format"

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    // =========================================================
    // NEW: Handle Generic Exception (Fallback Safety)
    // =========================================================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {

        Map<String, Object> response = new HashMap<>();

        response.put("message", "Something went wrong");
        response.put("details", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}