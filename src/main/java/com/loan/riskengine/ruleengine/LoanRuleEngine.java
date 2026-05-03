package com.loan.riskengine.ruleengine;

import com.loan.riskengine.entity.LoanRuleEntity;
// import com.loan.riskengine.entity.RuleCondition; // ❌ OLD (not used now)
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

    // Logger → used for debugging and tracing flow
    private static final Logger logger = LoggerFactory.getLogger(LoanRuleEngine.class);

    // =========================================================
    // MAIN METHOD → evaluates loan against rules
    // =========================================================
    public String evaluate(LoanApplication loan) {

        logger.info("Starting rule evaluation for Loan: income={}, creditScore={}",
                loan.getIncome(), loan.getCreditScore());

        // STEP 1: Fetch rules sorted by priority
        List<LoanRuleEntity> rules = ruleRepository.findAllByOrderByPriorityAsc();

        // STEP 2: Evaluate rules one by one
        for (LoanRuleEntity rule : rules) {

            /*
            ❌ OLD CONDITION-BASED ENGINE (kept for understanding)

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

            // ✅ NEW: Expression-based evaluation
            if (evaluateExpression(rule.getExpression(), loan)) {

                logger.info("Rule matched → Decision={}", rule.getDecision());

                return rule.getDecision();
            }
        }

        // Default fallback
        logger.info("No rule matched → Default decision=REVIEW");

        return "REVIEW";
    }

    // =========================================================
    // Replace variables with actual values
    // =========================================================
    private boolean evaluateExpression(String expression, LoanApplication loan) {

        // Replace variables dynamically
        expression = expression.replace("income", String.valueOf(loan.getIncome()));
        expression = expression.replace("creditScore", String.valueOf(loan.getCreditScore()));

        // Example:
        // "income >= 50000" → "60000 >= 50000"

        return evaluateSimpleExpression(expression);
    }

    // =========================================================
    // CORE ENGINE → handles brackets + AND/OR
    // =========================================================
    private boolean evaluateSimpleExpression(String expr) {

        try {

            /*
            ❌ OLD LOGIC (NO precedence + NO bracket support)

            if (expr.contains("AND")) { ... }
            if (expr.contains("OR")) { ... }
            */

            // =====================================================
            // ✅ STEP 1: HANDLE BRACKETS FIRST (RECURSIVE)
            // =====================================================
            while (expr.contains("(")) {

                int closeIndex = expr.indexOf(")");
                int openIndex = expr.lastIndexOf("(", closeIndex);

                // Extract inner expression
                String innerExpression = expr.substring(openIndex + 1, closeIndex);

                // Recursively evaluate inner part
                boolean innerResult = evaluateSimpleExpression(innerExpression);

                // Replace "(...)" with true/false
                expr = expr.substring(0, openIndex)
                     + innerResult
                     + expr.substring(closeIndex + 1);

                // Example:
                // "(60000 >= 50000 AND 750 >= 700)" → true
            }

            // =====================================================
            // ✅ STEP 2: HANDLE OR (LOW PRIORITY)
            // =====================================================
            String[] orParts = expr.split("OR");

            for (String orPart : orParts) {

                // =================================================
                // ✅ STEP 3: HANDLE AND (HIGH PRIORITY)
                // =================================================
                String[] andParts = orPart.split("AND");

                boolean andResult = true;

                for (String part : andParts) {

                    part = part.trim();

                    // Handle boolean replacements from brackets
                    if (part.equalsIgnoreCase("true")) continue;

                    if (part.equalsIgnoreCase("false")) {
                        andResult = false;
                        break;
                    }

                    // Evaluate actual condition
                    if (!evaluateCondition(part)) {
                        andResult = false;
                        break;
                    }
                }

                // If ANY OR block is true → return true
                if (andResult) return true;
            }

            return false;

        } catch (Exception e) {
            logger.error("Error evaluating expression: {}", expr, e);
            return false; // 🔥 prevents crash
        }
    }

    // =========================================================
    // Evaluate single condition like "60000 >= 50000"
    // =========================================================
    private boolean evaluateCondition(String condition) {

        try {
            // Normalize spacing (VERY IMPORTANT)
            condition = condition.replaceAll(">=", " >= ")
                                 .replaceAll("<=", " <= ")
                                 .replaceAll(">", " > ")
                                 .replaceAll("<", " < ")
                                 .replaceAll("==", " == ")
                                 .replaceAll("\\s+", " ")
                                 .trim();

            String[] tokens = condition.split(" ");

            // Safety check
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
            return false; // no crash
        }
    }
}