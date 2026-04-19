package com.loan.riskengine.entity;

import jakarta.persistence.*;

@Entity
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String applicantName;

    private double income;

    private int creditScore;

    private double loanAmount;

    private String status;

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