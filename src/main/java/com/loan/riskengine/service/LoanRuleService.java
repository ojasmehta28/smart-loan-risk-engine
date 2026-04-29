package com.loan.riskengine.service;

import com.loan.riskengine.entity.LoanRuleEntity;
import com.loan.riskengine.repository.LoanRuleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; // mark the class as a service layer, which typically contains business logic and interacts with repositories to perform operations on data.
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service 
public class LoanRuleService {

    @Autowired
    private LoanRuleRepository ruleRepository;

    private static final Logger logger = LoggerFactory.getLogger(LoanRuleService.class);

    // Create rule
    public LoanRuleEntity addRule(LoanRuleEntity rule){

        logger.info("Adding new rule: {}", rule);

        return ruleRepository.save(rule);
    }

    // Read rules
    public List<LoanRuleEntity> getAllRules(){

        logger.info("Fetching all rules");

        return ruleRepository.findAllByOrderByPriorityAsc();
    }

    // Update rule
    public LoanRuleEntity updateRule(Long id, LoanRuleEntity updatedRule){

        logger.info("Updating rule with id: {}", id);

        LoanRuleEntity existing = ruleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rule not found"));

        existing.setField(updatedRule.getField());
        existing.setOperator(updatedRule.getOperator());
        existing.setValue(updatedRule.getValue());
        existing.setDecision(updatedRule.getDecision());
        existing.setPriority(updatedRule.getPriority());

        return ruleRepository.save(existing);
    }

    // Delete rule
    public void deleteRule(Long id){

        logger.info("Deleting rule with id: {}", id);

        ruleRepository.deleteById(id);
    }
}
