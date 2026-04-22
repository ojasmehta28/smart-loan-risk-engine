package com.loan.riskengine.controller; // controller class for handling loan applications 

import com.loan.riskengine.dto.LoanRequestDTO;
import com.loan.riskengine.dto.LoanResponseDTO;
import com.loan.riskengine.service.LoanService;

import org.springframework.beans.factory.annotation.Autowired; // autowired annotation for dependency injection
import org.springframework.web.bind.annotation.*; // annotations for REST controller and request mapping
import jakarta.validation.Valid; // constraint validation for request body 

@RestController // Restcontroller handles HTTP requests and responses in a RESTful manner
@RequestMapping("/loan") // base URL for all endpoints in this controller
public class LoanController { 

    @Autowired
    private LoanService loanService; // inject the LoanService to handle business logic

    @PostMapping("/apply") // endpoint for applying for a loan, accepts POST requests at /loan/apply
    public LoanResponseDTO applyLoan(@Valid @RequestBody LoanRequestDTO request) { // method to handle loan application, validates the request body 
                                                                                   // and maps it to LoanRequestDTO
        return loanService.applyLoan(request);
    }
}