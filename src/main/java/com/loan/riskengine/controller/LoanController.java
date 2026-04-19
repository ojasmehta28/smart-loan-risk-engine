package com.loan.riskengine.controller;

import com.loan.riskengine.entity.LoanApplication; // Entity class representing a loan application
import com.loan.riskengine.service.LoanService; // Service layer to handle business logic for loan applications
import org.springframework.beans.factory.annotation.Autowired; // Dependency injection for the service layer
import org.springframework.web.bind.annotation.*; //rest controller to handle HTTP requests related to loan applications 
import jakarta.validation.*; // Validation annotations for request body validation

@RestController
@RequestMapping("/loan") // Base URL for all loan-related endpoints
public class LoanController {

    @Autowired
    private LoanService loanService; // Injecting the LoanService to handle business logic for loan applications

    @PostMapping("/apply")
    public LoanApplication applyLoan(@Valid @RequestBody LoanApplication loan) { // Endpoint to apply for a loan,
                                                                                 //  validates the request body and 
                                                                                 // processes it through the service layer
        return loanService.applyLoan(loan);
    }
}