package com.loan.riskengine.ruleengine;

import com.loan.riskengine.entity.LoanRuleEntity;
import com.loan.riskengine.repository.LoanRuleRepository;
import com.loan.riskengine.entity.LoanApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoanRuleEngine {

    @Autowired
    private LoanRuleRepository ruleRepository;

    public String evaluate(LoanApplication loan) {

        // STEP 1: Get rules sorted by priority
        List<LoanRuleEntity> rules = ruleRepository.findAllByOrderByPriorityAsc();

        // STEP 2: Evaluate each rule
        for (LoanRuleEntity rule : rules) {

            // Get value dynamically (income / creditScore)
            double loanValue = getFieldValue(loan, rule.getField());

            // Apply operator logic
            if (evaluateCondition(loanValue, rule.getOperator(), rule.getValue())) {
                return rule.getDecision();
            }
        }

        // STEP 3: Default case
        return "REVIEW";
    }

    
    // Get field value dynamically
    
    private double getFieldValue(LoanApplication loan, String field) { // This method retrieves the value of the specified field from the loan application. It uses a switch statement to determine which field to access based on the provided field name.

        switch (field) {

            case "income":
                return loan.getIncome();

            case "creditScore":
                return loan.getCreditScore();

            default:
                return 0;
        }
    }

    // Evaluate operator dynamically
    private boolean evaluateCondition(double loanValue, String operator, double ruleValue) { // This method evaluates the condition specified by the operator between the loan value and the rule value. It uses a switch statement to determine which comparison to perform based on the provided operator.

        switch (operator) {

            case ">":
                return loanValue > ruleValue;

            case "<":
                return loanValue < ruleValue;

            case ">=":
                return loanValue >= ruleValue;

            case "<=":
                return loanValue <= ruleValue;

            case "==":
                return loanValue == ruleValue;

            default:
                return false;
        }
    }
}