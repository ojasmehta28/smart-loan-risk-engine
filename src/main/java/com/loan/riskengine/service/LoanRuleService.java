package com.loan.riskengine.service;

import com.loan.riskengine.entity.LoanRuleEntity;
import com.loan.riskengine.repository.LoanRuleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; // mark the class as a service layer, which typically contains business logic and interacts with repositories to perform operations on data.
import java.util.List;

import org.slf4j.Logger; //used for logging information, warnings, and errors in the application. It helps in tracking the flow of the application and debugging issues.
import org.slf4j.LoggerFactory; //used for creating Logger instances. It provides a way to obtain a logger for a specific class, which can then be used to log messages with various levels of severity (e.g., info, debug, error).

import org.springframework.data.domain.Page; // represents a page of data, typically used for pagination. It contains information about the total number of pages, the current page number, and the list of items on that page.
import org.springframework.data.domain.PageRequest; // used to create a PageRequest object, which specifies the page number and page size for pagination. It is often used in repository methods to fetch a specific page of data from the database.
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

        //existing.setField(updatedRule.getField());
        //existing.setOperator(updatedRule.getOperator());
        //existing.setValue(updatedRule.getValue());
        existing.setDecision(updatedRule.getDecision());
        existing.setPriority(updatedRule.getPriority());

        return ruleRepository.save(existing);
    }

    // Delete rule
    public void deleteRule(Long id){

        logger.info("Deleting rule with id: {}", id);

        ruleRepository.deleteById(id); 
    }

    // Paginated rules
    public Page<LoanRuleEntity> getRulesPaginated(int page, int size) {

    // PageRequest.of(page, size)
    // page = which page (0 = first page)
    // size = how many records per page

    return ruleRepository.findAll(PageRequest.of(page, size)); // fetches a page of rules based on the provided page number and size.
    }
}
