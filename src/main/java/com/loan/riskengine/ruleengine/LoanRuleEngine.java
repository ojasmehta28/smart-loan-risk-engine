package com.loan.riskengine.ruleengine; // Package for defining rules and logic related to loan risk assessment

import com.loan.riskengine.entity.LoanApplication; // Evaluate against rules
import java.util.ArrayList; // Managing a list of loan rules
import java.util.List; // Defining a list of loan rules

public class LoanRuleEngine { // Class to evaluate loan applications against defined rules and make decisions based on those rules
    private List<LoanRule> rules= new ArrayList<>(); // List to hold the loan rules

    public LoanRuleEngine() { // Constructor to initialize the rule engine with some default rules
        
        //Rule 1: High Income & Good Credit Score → APPROVED
        rules.add(new LoanRule(50000, 700, "APPROVED")); 

        //Rule 2: Low Credit Score → REJECTED
        rules.add(new LoanRule(0, 500, "REJECTED"));

    }

    public String evaluate(LoanApplication loan) { // Method to evaluate a loan application 
                                                   // against the defined rules and return a decision
        for (LoanRule rule : rules) { // Iterate through each rule in the list
            if (loan.getIncome() >= rule.getMinIncome() && loan.getCreditScore() >= rule.getMinCreditScore()) { // Check if the loan application meets the criteria of the current rule
                return rule.getDecision(); // If it does, return the decision associated with that rule
            }
        }
        return "REVIEW"; // If no rules match, return "REVIEW" as the default decision
        


    }

    
}
