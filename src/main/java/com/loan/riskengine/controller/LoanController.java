package com.loan.riskengine.controller;

import com.loan.riskengine.entity.LoanApplication;
import com.loan.riskengine.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping("/apply")
    public LoanApplication applyLoan(@RequestBody LoanApplication loan) {
        return loanService.applyLoan(loan);
    }
}