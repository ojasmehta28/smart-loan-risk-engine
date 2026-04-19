package com.loan.riskengine.entity;

import jakarta.persistence.*; // Import JPA annotations
import jakarta.validation.constraints.*; // Import validation annotations

@Entity
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate ID values 
     private Long id;

    @NotBlank(message = "Applicant name cannot be empty") // Validation for non-empty name
    private String applicantName;

    @Min(value = 1, message = "Income must be greater than 0") // Validation for positive income
    private double income;

    @Min(value = 300, message = "Credit score must be at least 300") // Validation for minimum credit score
    @Max(value = 900, message = "Credit score cannot exceed 900") // Validation for maximum credit score
    private int creditScore;

    @Min(value = 1, message = "Loan amount must be greater than 0") // Validation for positive loan amount
    private double loanAmount;

    private String status; // e.g., "PENDING", "APPROVED", "REJECTED"

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getApplicantName() {
        return applicantName;
    }
    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public double getIncome() {
        return income;
    }
    public void setIncome(double income) {
        this.income = income;
    }
    public int getCreditScore() {
        return creditScore;
    }
    public void setCreditScore(int creditScore){
        this.creditScore = creditScore;
    }
    public double getLoanAmount(){
        return loanAmount;
    }
    public void setLoanAmount(double loanAmount){
        this.loanAmount = loanAmount;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

}