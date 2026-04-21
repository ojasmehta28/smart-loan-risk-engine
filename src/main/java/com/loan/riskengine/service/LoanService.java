package com.loan.riskengine.service;

import com.loan.riskengine.dto.LoanRequestDTO;
import com.loan.riskengine.dto.LoanResponseDTO;
import com.loan.riskengine.entity.LoanApplication;
import com.loan.riskengine.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired; //dependency injection for the repository
import org.springframework.stereotype.Service; //business logic layer

@Service
public class LoanService {
    
    @Autowired //dependency injection for the repository
    private LoanRepository loanRepository; //repository to interact with the database
    
    public LoanResponseDTO applyloan(LoanRequestDTO request){ //method to process loan application

        //Step 1: Convert DTO to Entity
        LoanApplication loan = new LoanApplication(); //create a new loan application entity
        loan.setApplicantName(request.getApplicantName()); //set applicant name from the request DTO
        loan.setIncome(request.getApplicantIncome()); //set income from the request DTO
        loan.setCreditScore(request.getCreditScore()); ///set credit score from the request DTO
        loan.setLoanAmount(request.getLoanAmount()); //set loan amount from the request DTO

        //Step 2: Business Logic to determine loan status
        if (loan.getIncome() > 50000 && loan.getCreditScore() > 700) {
            loan.setStatus("APPROVED");
        } else if (loan.getCreditScore() < 500) {
            loan.setStatus("REJECTED");
        } else {
            loan.setStatus("REVIEW");
        }
    


        //Step 3: Save to Database
        LoanApplication saved= loanRepository.save(loan); //save the loan application entity to the database 
                                                          // and get the saved entity with generated ID

        //Step 4: Convert Entity back to DTO
        LoanResponseDTO response = new LoanResponseDTO(); //create a new response DTO
        response.setId(saved.getId()); //set the generated ID from the saved entity
        response.setApplicantName(saved.getApplicantName()); //set applicant name from the saved entity
        response.setIncome(saved.getIncome()); //set income from the saved entity
        response.setCreditScore(saved.getCreditScore()); //set credit score from the saved entity
        response.setLoanAmount(saved.getLoanAmount()); //set loan amount from the saved entity
        response.setStatus(saved.getStatus()); //set loan status from the saved entity

        return response;
    }
}