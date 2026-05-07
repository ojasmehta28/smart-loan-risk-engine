package com.loan.riskengine.service;

import com.loan.riskengine.entity.LoanRuleEntity;
// import com.loan.riskengine.entity.RuleCondition; // (conditions removed)
import com.loan.riskengine.repository.LoanRuleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Service 
public class LoanRuleService {

    @Autowired
    private LoanRuleRepository ruleRepository;

    private static final Logger logger = LoggerFactory.getLogger(LoanRuleService.class);

    
    

    // CREATE RULE    
    public LoanRuleEntity addRule(LoanRuleEntity rule){

        logger.info("Adding new rule: {}", rule);

        // Validate expression-based rule BEFORE saving to DB
        validateRule(rule);

        // (conditions mapping)
        /*
        for (RuleCondition cond : rule.getConditions()) {
            cond.setRule(rule);
        }
        */

        return ruleRepository.save(rule);
    }

    
    
    
    // READ RULES
    public List<LoanRuleEntity> getAllRules(){

        logger.info("Fetching all rules");

        return ruleRepository.findAllByOrderByPriorityAsc();
    }

   
    
   
    // UPDATE RULE
    public LoanRuleEntity updateRule(Long id, LoanRuleEntity updatedRule){

        logger.info("Updating rule with id: {}", id);

        LoanRuleEntity existing = ruleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rule not found"));

        // (single-condition logic)
        // existing.setField(updatedRule.getField());
        // existing.setOperator(updatedRule.getOperator());
        // existing.setValue(updatedRule.getValue());

        // (AND/OR condition logic)
        // existing.setLogicalOperator(updatedRule.getLogicalOperator());
        // existing.setConditions(updatedRule.getConditions());

        // Validate expression before updating
        validateRule(updatedRule);

        
        existing.setDecision(updatedRule.getDecision());
        existing.setPriority(updatedRule.getPriority());
        existing.setExpression(updatedRule.getExpression());

        return ruleRepository.save(existing);
    }

    
    
    // DELETE RULE
    public void deleteRule(Long id){

        logger.info("Deleting rule with id: {}", id);

        ruleRepository.deleteById(id); 
    }

    
    
    // PAGINATED READ
    public Page<LoanRuleEntity> getRulesPaginated(int page, int size) {

        return ruleRepository.findAll(PageRequest.of(page, size));
    }

    
    
    // VALIDATION LOGIC 
    private void validateRule(LoanRuleEntity rule) {

        
        // 1. Expression must NOT be null or empty
        if (rule.getExpression() == null || rule.getExpression().trim().isEmpty()) {
            throw new RuntimeException("Expression cannot be empty");
        }

        // Old  Validation
        /*
        String expr = rule.getExpression();

        if (!(expr.contains("income") || expr.contains("creditScore"))) {
            throw new RuntimeException("Expression must contain valid fields");
        }

        if (!(expr.contains(">") || expr.contains("<") || expr.contains("=="))) {
            throw new RuntimeException("Expression must contain valid operator");
        }
        */

        

        String expr = rule.getExpression().trim();

        // 2. Must contain valid fields (prevents "salary", "abc")
        if (!(expr.contains("income") || expr.contains("creditScore"))) {
            throw new RuntimeException("Expression must contain valid fields (income / creditScore)");
        }

        // 3. Must contain at least one valid operator
        if (!(expr.contains(">") || expr.contains("<") || expr.contains("=="))) {
            throw new RuntimeException("Expression must contain a valid operator (>, <, >=, <=, ==)");
        }

        // 4. Detect missing operator (example: "income 50000")
        // Regex explanation:
        // \b(income|creditScore) → word match
        // \s+\d+ → space followed by number
        if (expr.matches(".*\\b(income|creditScore)\\s+\\d+.*")) {
            throw new RuntimeException("Invalid expression format. Operator missing");
        }

        // 5. Detect invalid operator patterns (like >>>, <<<)
        if (expr.contains(">>>") || expr.contains("<<<")) {
            throw new RuntimeException("Invalid operator format");
        }

        
        // 6. Prevent duplicate priority
        List<LoanRuleEntity> existingRules = ruleRepository.findAll();

        for (LoanRuleEntity existing : existingRules) {
            if (existing.getPriority() == rule.getPriority()) {
                throw new RuntimeException("Rule with same priority already exists");
            }
        }
    }
}