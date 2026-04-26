package com.loan.riskengine.ruleengine; // Package for defining rules and logic related to loan risk assessment

import com.loan.riskengine.entity.LoanRuleEntity; // Entity class representing a loan rule in the database
import com.loan.riskengine.repository.LoanRuleRepository; // Repository interface for accessing loan rules from the database
import com.loan.riskengine.entity.LoanApplication; // Entity class representing a loan application that needs to be evaluated against the rules
import org.springframework.beans.factory.annotation.Autowired; // Annotation for dependency injection
import org.springframework.stereotype.Component; // Spring component for dependency injection
//import java.util.ArrayList; // Managing a list of loan rules
import java.util.List; // Defining a list of loan rules

@Component 
public class LoanRuleEngine { // Class to evaluate loan applications against defined rules and make decisions based on those rules
    // private List<LoanRule> rules= new ArrayList<>(); // List to hold the loan rules

    // public LoanRuleEngine() { // Constructor to initialize the rule engine with some default rules
        
    //     //Rule 1: High Income & Good Credit Score → APPROVED
    //     rules.add(new LoanRule(50000, 700, "APPROVED")); 

    //     //Rule 2: Low Credit Score → REJECTED
    //     rules.add(new LoanRule(0, 500, "REJECTED"));

    // }
    @Autowired
    private LoanRuleRepository ruleRepository; // Repository to access loan rules from the database


    public String evaluate(LoanApplication loan) { // Method to evaluate a loan application 
                                                   // against the defined rules and return a decision
        List<LoanRuleEntity> rules = ruleRepository.findAll(); // Fetch all loan rules from the database
        for (LoanRuleEntity rule : rules) { // Iterate through each rule in the list
        
            if (loan.getIncome() >= rule.getMinIncome() && loan.getCreditScore() >= rule.getMinCreditScore()) { // Check if the loan application meets the criteria of the current rule
                return rule.getDecision(); // If it does, return the decision associated with that rule
            }
        }
        return "REVIEW"; // If no rules match, return "REVIEW" as the default decision
        


    }

    
}
