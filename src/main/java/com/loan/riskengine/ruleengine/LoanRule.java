package com.loan.riskengine.ruleengine; // Package for defining rules and logic related to loan risk assessment

import lombok.*; // Import Lombok annotations

@Data // Lombok annotation to generate getters & setters
@NoArgsConstructor // Lombok annotation to generate a no-args constructor
public class LoanRule { // Class to encapsulate the rules for evaluating loan applications
    private double minIncome;
    private int minCreditScore;
    private String decision;

    public LoanRule(double minIncome, int minCreditScore, String decision) {
        this.minIncome = minIncome;
        this.minCreditScore = minCreditScore; 
        this.decision = decision;
    }

}
