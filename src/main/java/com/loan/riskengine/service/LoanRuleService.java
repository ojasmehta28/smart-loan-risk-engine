package com.loan.riskengine.service;

import com.loan.riskengine.entity.LoanRuleEntity;
import com.loan.riskengine.entity.RuleCondition;
import com.loan.riskengine.repository.LoanRuleRepository;

import org.springframework.beans.factory.annotation.Autowired; // Autowired annotation for dependency injection
import org.springframework.stereotype.Service; // Service annotation indicates that this class is a service component in the Spring context, responsible for business logic related to loan rules.
import java.util.List;

import org.slf4j.Logger; // Logger interface from SLF4J (Simple Logging Facade for Java) used for logging messages in the application.
import org.slf4j.LoggerFactory; // LoggerFactory is a utility class from SLF4J used to create Logger instances. It provides methods to obtain a logger for a specific class or name, allowing developers to log messages with different levels (e.g., info, debug, error) throughout the application.

import org.springframework.data.domain.Page; // represents a page of data, typically used for pagination. It contains information about the total number of pages, the current page number, and the list of items on that page.
import org.springframework.data.domain.PageRequest; // used to create a PageRequest object, which specifies the page number and page size for pagination. It is often used in conjunction with Spring Data repositories to retrieve paginated results from the database.

@Service 
public class LoanRuleService {

    @Autowired
    private LoanRuleRepository ruleRepository;

    private static final Logger logger = LoggerFactory.getLogger(LoanRuleService.class);

    
    // CREATE RULE
    public LoanRuleEntity addRule(LoanRuleEntity rule){

        logger.info("Adding new rule: {}", rule);

        // ✅ NEW: Validate before saving
        validateRule(rule);

        // ✅ NEW: Set rule reference in each condition (IMPORTANT)
        for (RuleCondition cond : rule.getConditions()) {
            cond.setRule(rule);
        }

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

        // OLD (single-condition logic — kept for reference)
        // existing.setField(updatedRule.getField());
        // existing.setOperator(updatedRule.getOperator());
        // existing.setValue(updatedRule.getValue());

        // NEW: Validate updated rule
        validateRule(updatedRule);

        // NEW: Update fields
        existing.setDecision(updatedRule.getDecision());
        existing.setPriority(updatedRule.getPriority());
        existing.setLogicalOperator(updatedRule.getLogicalOperator());

        // NEW: Update conditions
        existing.setConditions(updatedRule.getConditions());

        // IMPORTANT: Set back reference again
        for (RuleCondition cond : existing.getConditions()) {
            cond.setRule(existing);
        }

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

        // 1. Logical operator validation
        if (!(rule.getLogicalOperator().equalsIgnoreCase("AND") ||
              rule.getLogicalOperator().equalsIgnoreCase("OR"))) {

            throw new RuntimeException("Invalid logical operator. Use AND / OR");
        }

        // 2. Conditions must exist
        if (rule.getConditions() == null || rule.getConditions().isEmpty()) {
            throw new RuntimeException("Rule must have at least one condition");
        }

        // 3. Validate each condition
        for (RuleCondition cond : rule.getConditions()) {

            // Field validation
            if (!(cond.getField().equals("income") ||
                  cond.getField().equals("creditScore"))) {

                throw new RuntimeException("Invalid field: " + cond.getField());
            }

            // Operator validation
            if (!(cond.getOperator().equals(">") ||
                  cond.getOperator().equals("<") ||
                  cond.getOperator().equals(">=") ||
                  cond.getOperator().equals("<=") ||
                  cond.getOperator().equals("=="))) {

                throw new RuntimeException("Invalid operator: " + cond.getOperator());
            }

            // Value validation
            if (cond.getValue() < 0) {
                throw new RuntimeException("Value cannot be negative");
            }
        }

        // 4. OPTIONAL: Prevent duplicate priority
        List<LoanRuleEntity> existingRules = ruleRepository.findAll();

        for (LoanRuleEntity existing : existingRules) {
            if (existing.getPriority() == rule.getPriority()) {
                throw new RuntimeException("Rule with same priority already exists");
            }
        }
    }
}