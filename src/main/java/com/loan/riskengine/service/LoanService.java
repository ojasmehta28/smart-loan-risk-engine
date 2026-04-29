package com.loan.riskengine.service;

import com.loan.riskengine.dto.LoanRequestDTO;
import com.loan.riskengine.dto.LoanResponseDTO;
import com.loan.riskengine.entity.LoanApplication;
import com.loan.riskengine.repository.LoanRepository;
import com.loan.riskengine.ruleengine.LoanRuleEngine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanRuleEngine ruleEngine;

    // Logger
    private static final Logger logger = LoggerFactory.getLogger(LoanService.class);

    public LoanResponseDTO applyLoan(LoanRequestDTO request) {

        logger.info("Received loan request for applicant: {}", request.getApplicantName());

        LoanApplication loan = new LoanApplication();

        loan.setApplicantName(request.getApplicantName());
        loan.setIncome(request.getIncome());
        loan.setCreditScore(request.getCreditScore());
        loan.setLoanAmount(request.getLoanAmount());

        // Evaluate rules
        loan.setStatus(ruleEngine.evaluate(loan));

        logger.info("Loan decision for {} is {}", request.getApplicantName(), loan.getStatus());

        LoanApplication saved = loanRepository.save(loan);

        LoanResponseDTO response = new LoanResponseDTO();

        response.setId(saved.getId());
        response.setApplicantName(saved.getApplicantName());
        response.setIncome(saved.getIncome());
        response.setCreditScore(saved.getCreditScore());
        response.setLoanAmount(saved.getLoanAmount());
        response.setStatus(saved.getStatus());

        return response;
    }
}