package com.loan.riskengine.service;

import com.loan.riskengine.entity.LoanApplication;
import com.loan.riskengine.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; //business logic layer

@Service
public class LoanService {

    @Autowired //dependency injection for the repository
    private LoanRepository loanRepository;

    public LoanApplication applyLoan(LoanApplication loan) {

        if (loan.getIncome() > 50000 && loan.getCreditScore() > 700) {
            loan.setStatus("APPROVED");
        } else if (loan.getCreditScore() < 500) {
            loan.setStatus("REJECTED");
        } else {
            loan.setStatus("REVIEW");
        }

        return loanRepository.save(loan);
    }
}