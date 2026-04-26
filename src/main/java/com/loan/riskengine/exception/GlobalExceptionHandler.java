package com.loan.riskengine.exception;
// I implemented centralized exception handling using @RestControllerAdvice and @ExceptionHandler to provide structured validation error responses 
import org.springframework.web.bind.annotation.*; // Annotations for global exception handling in a Spring Boot application 
import org.springframework.http.ResponseEntity; // Class to represent HTTP responses, allowing us to return custom error messages and status codes
import org.springframework.http.HttpStatus; // Enum representing HTTP status codes, used to indicate the result of an HTTP request 
import org.springframework.web.bind.MethodArgumentNotValidException; // Exception thrown when validation on an argument annotated with @Valid fails, 
                                                                     // used to handle validation errors in the application 

import java.util.HashMap; // Utility class for creating a map to hold error messages and details 
import java.util.Map; // Interface for a map to hold error messages and details, used in the response body of error responses

@RestControllerAdvice // Global exception handler for the application to handle exceptions 
                      // across all controllers and provide consistent error responses 
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class) // Method to handle validation exceptions that occur when @Valid fails in controller methods, 
                                         // allowing us to return a structured error response with details about the validation errors
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {  

        Map<String, String> errors = new HashMap<>(); // Create a map to hold field-specific error messages, 
                                                      // where the key is the field name and the value is the error message

        ex.getBindingResult().getFieldErrors().forEach(error -> { // Iterate over the field errors from the exception and populate the 
                                                                  // errors map with field names and their corresponding error messages
            errors.put(error.getField(), error.getDefaultMessage()); // The getField() method retrieves the name of the field that caused 
                                                                    // the validation error, and getDefaultMessage() retrieves the default 
                                                                    // error message associated with that validation failure. By putting 
                                                                    // these into the errors map, we can create a structured response that 
                                                                    // clearly indicates which fields had validation issues and what those 
                                                                    // issues were, making it easier for clients to understand and correct 
                                                                    // their input.
        });

        Map<String, Object> response = new HashMap<>(); // Create a response map to hold the overall error response, including a 
                                                        // general message and the specific field errors
        response.put("message", "Validation failed"); 
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // Return a ResponseEntity with the error response map 
                                                                       // and a 400 Bad Request status, indicating that the 
                                                                       // client's request was invalid due to validation errors
    }
}