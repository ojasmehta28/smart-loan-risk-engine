package com.loan.riskengine.service;

import com.loan.riskengine.ruleengine.LoanRuleEngine; 
import com.loan.riskengine.dto.LoanRequestDTO;
import com.loan.riskengine.dto.LoanResponseDTO;
import com.loan.riskengine.entity.LoanApplication;
import com.loan.riskengine.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; // Service annotation to mark this class as a service component in the Spring context

@Service 
public class LoanService { // Service class to handle business logic for loan applications

    @Autowired
    private LoanRepository loanRepository;

    public LoanResponseDTO applyLoan(LoanRequestDTO request) {

        // STEP 1: DTO → Entity
        LoanApplication loan = new LoanApplication();

        loan.setApplicantName(request.getApplicantName());
        loan.setIncome(request.getIncome()); 
        loan.setCreditScore(request.getCreditScore());
        loan.setLoanAmount(request.getLoanAmount());

        // STEP 2: Business Logic
        LoanRuleEngine engine = new LoanRuleEngine();
        loan.setStatus(engine.evaluate(loan));

        // STEP 3: Save
        LoanApplication saved = loanRepository.save(loan);

        // STEP 4: Entity → DTO
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