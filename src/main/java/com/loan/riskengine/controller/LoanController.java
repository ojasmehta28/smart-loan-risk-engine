package com.loan.riskengine.controller;

import com.loan.riskengine.dto.LoanRequestDTO;
import com.loan.riskengine.dto.LoanResponseDTO;
import com.loan.riskengine.service.LoanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping("/apply")
    public LoanResponseDTO applyLoan(@Valid @RequestBody LoanRequestDTO request) {
        return loanService.applyLoan(request);
    }
}