package com.loan.riskengine.service;

import com.loan.riskengine.dto.LoanRequestDTO;
import com.loan.riskengine.dto.LoanResponseDTO;
import com.loan.riskengine.entity.LoanApplication;
import com.loan.riskengine.repository.LoanRepository;
import com.loan.riskengine.ruleengine.LoanRuleEngine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

    // Repository to interact with database (LoanApplication table)
    @Autowired
    private LoanRepository loanRepository;

    // Inject Rule Engine (managed by Spring)
    @Autowired
    private LoanRuleEngine ruleEngine;

    public LoanResponseDTO applyLoan(LoanRequestDTO request) {

        // STEP 1: Convert DTO → Entity

        // Create entity object (this will be saved in DB)
        LoanApplication loan = new LoanApplication();

        // Map incoming request data to entity
        loan.setApplicantName(request.getApplicantName());
        loan.setIncome(request.getIncome());
        loan.setCreditScore(request.getCreditScore());
        loan.setLoanAmount(request.getLoanAmount());

        // STEP 2: Apply Dynamic Rule Engine

        // Instead of hardcoded if-else, delegate decision to rule engine
        // RuleEngine will fetch rules from DB and evaluate
        loan.setStatus(ruleEngine.evaluate(loan));

        // STEP 3: Save to Database

        // Save loan into DB and get saved object (with generated ID)
        LoanApplication saved = loanRepository.save(loan);

        // STEP 4: Convert Entity → Response DTO

        LoanResponseDTO response = new LoanResponseDTO();

        // Map saved entity data to response
        response.setId(saved.getId());
        response.setApplicantName(saved.getApplicantName());
        response.setIncome(saved.getIncome());
        response.setCreditScore(saved.getCreditScore());
        response.setLoanAmount(saved.getLoanAmount());
        response.setStatus(saved.getStatus());

        return response;
    }
}