package com.loan.riskengine.ruleengine;

import com.loan.riskengine.entity.LoanRuleEntity;
import com.loan.riskengine.entity.RuleCondition;
import com.loan.riskengine.repository.LoanRuleRepository;
import com.loan.riskengine.entity.LoanApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Component
public class LoanRuleEngine {

    @Autowired
    private LoanRuleRepository ruleRepository;

    // Logger object (used to print logs)
    private static final Logger logger = LoggerFactory.getLogger(LoanRuleEngine.class);

    public String evaluate(LoanApplication loan) {

        // Log start of evaluation
        logger.info("Starting rule evaluation for Loan: income={}, creditScore={}",
                loan.getIncome(), loan.getCreditScore());

        // STEP 1: Get rules sorted by priority
        List<LoanRuleEntity> rules = ruleRepository.findAllByOrderByPriorityAsc();

        // STEP 2: Evaluate each rule
        for (LoanRuleEntity rule : rules) {

            boolean result;

            if ("AND".equalsIgnoreCase(rule.getLogicalOperator())) {
            result = rule.getConditions().stream()
                .allMatch(cond -> evaluateSingleCondition(loan, cond));
            } else {
                result = rule.getConditions().stream()
                        .anyMatch(cond -> evaluateSingleCondition(loan, cond));
            }
        
            if (result) {
                return rule.getDecision();
            }
        }

        // Log fallback
        logger.info("No rule matched → Default decision=REVIEW");

        return "REVIEW";
    }

    private double getFieldValue(LoanApplication loan, String field) {

        switch (field) {

            case "income":
                return loan.getIncome();

            case "creditScore":
                return loan.getCreditScore();

            default:
                return 0;
        }
    }

    private boolean evaluateCondition(double loanValue, String operator, double ruleValue) {

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
    private boolean evaluateSingleCondition(LoanApplication loan, RuleCondition cond) {

    double loanValue = getFieldValue(loan, cond.getField());

    return evaluateCondition(loanValue, cond.getOperator(), cond.getValue());
    }
}