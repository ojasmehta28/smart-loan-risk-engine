package com.loan.riskengine.ruleengine;

import com.loan.riskengine.entity.LoanRuleEntity;
// import com.loan.riskengine.entity.RuleCondition; // ❌ OLD
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

    private static final Logger logger = LoggerFactory.getLogger(LoanRuleEngine.class);

    public String evaluate(LoanApplication loan) {

        logger.info("Starting rule evaluation for Loan: income={}, creditScore={}",
                loan.getIncome(), loan.getCreditScore());

        // STEP 1: Get rules sorted by priority
        List<LoanRuleEntity> rules = ruleRepository.findAllByOrderByPriorityAsc();

        // STEP 2: Evaluate each rule
        for (LoanRuleEntity rule : rules) {

            
            /*
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
            */

            
            if (evaluateExpression(rule.getExpression(), loan)) { // used to be evaluateConditions 

                logger.info("Rule matched → Decision={}", rule.getDecision());

                return rule.getDecision();
            }
        }

        logger.info("No rule matched → Default decision=REVIEW");

        return "REVIEW";
    }

    
    // Evaluate full expression
    private boolean evaluateExpression(String expression, LoanApplication loan) {

        // Replace variables with actual values
        expression = expression.replace("income", String.valueOf(loan.getIncome()));
        expression = expression.replace("creditScore", String.valueOf(loan.getCreditScore()));

        // Example:
        // "income >= 50000" → "60000 >= 50000"

        return evaluateSimpleExpression(expression);
    }

    
    // Handle AND / OR
    // private boolean evaluateSimpleExpression(String expr) {

    //     // AND logic
    //     if (expr.contains("AND")) {

    //         String[] parts = expr.split("AND");

    //         for (String part : parts) {
    //             if (!evaluateCondition(part.trim())) {
    //                 return false;
    //             }
    //         }
    //         return true;
    //     }

    //     // OR logic
    //     if (expr.contains("OR")) {

    //         String[] parts = expr.split("OR");

    //         for (String part : parts) {
    //             if (evaluateCondition(part.trim())) {
    //                 return true;
    //             }
    //         }
    //         return false;
    //     }

    //     // Single condition
    //     return evaluateCondition(expr);
    // }
    private boolean evaluateSimpleExpression(String expr) {

    try {

        if (expr.contains("AND")) {
            String[] parts = expr.split("AND");

            for (String part : parts) {
                if (!evaluateCondition(part.trim())) {
                    return false;
                }
            }
            return true;
        }

        if (expr.contains("OR")) {
            String[] parts = expr.split("OR");

            for (String part : parts) {
                if (evaluateCondition(part.trim())) {
                    return true;
                }
            }
            return false;
        }

        return evaluateCondition(expr);

    } catch (Exception e) {
        logger.error("Error evaluating expression: {}", expr, e);
        return false; // 🔥 NO CRASH
    }
}

    
    // Evaluate single condition
    // private boolean evaluateCondition(String condition) {

    //     // Example: "60000 >= 50000"

    //     String[] tokens = condition.split(" ");

    //     double left = Double.parseDouble(tokens[0]);
    //     String operator = tokens[1];
    //     double right = Double.parseDouble(tokens[2]);

    //     switch (operator) {

    //         case ">":
    //             return left > right;

    //         case "<":
    //             return left < right;

    //         case ">=":
    //             return left >= right;

    //         case "<=":
    //             return left <= right;

    //         case "==":
    //             return left == right;

    //         default:
    //             return false;
    //     }
    // }

    private boolean evaluateCondition(String condition) {

        try {
        // Normalize spacing (VERY IMPORTANT FIX)
        condition = condition.replaceAll(">=", " >= ")
                             .replaceAll("<=", " <= ")
                             .replaceAll(">", " > ")
                             .replaceAll("<", " < ")
                             .replaceAll("==", " == ")
                             .replaceAll("\\s+", " ")
                             .trim();

        String[] tokens = condition.split(" ");

        // SAFETY CHECK
        if (tokens.length != 3) {
            logger.error("Invalid condition format: {}", condition);
            return false;
        }

        double left = Double.parseDouble(tokens[0]);
        String operator = tokens[1];
        double right = Double.parseDouble(tokens[2]);

        switch (operator) {
            case ">": return left > right;
            case "<": return left < right;
            case ">=": return left >= right;
            case "<=": return left <= right;
            case "==": return left == right;
            default:
                logger.error("Invalid operator: {}", operator);
                return false;
        }

        } catch (Exception e) {
        logger.error("Error evaluating condition: {}", condition, e);
        return false; 
        }
    }

    
    
}