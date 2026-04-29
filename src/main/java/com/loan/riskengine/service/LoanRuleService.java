package com.loan.riskengine.service;

import com.loan.riskengine.entity.LoanRuleEntity;
import com.loan.riskengine.repository.LoanRuleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; // mark the class as a service layer, which typically contains business logic and interacts with repositories to perform operations on data.
import java.util.List;

@Service 
public class LoanRuleService {
    @Autowired
    private LoanRuleRepository ruleRepository;

    //Create rule
    public LoanRuleEntity addRule(LoanRuleEntity rule){
        return ruleRepository.save(rule);
    }

    //Read all rules
    public List<LoanRuleEntity> getAllRules(){ //List is used to return a collection of LoanRuleEntity objects
        return ruleRepository.findAllByOrderByPriorityAsc();
    }

    //Update rule
    public LoanRuleEntity updateRule(Long id, LoanRuleEntity updatedRule){

        LoanRuleEntity existing= ruleRepository.findById(id).orElseThrow(() -> new RuntimeException("Rule not found"));

        existing.setField(updatedRule.getField());
        existing.setOperator(updatedRule.getOperator());
        existing.setValue(updatedRule.getValue());
        existing.setDecision(updatedRule.getDecision());
        existing.setPriority(updatedRule.getPriority());
        return ruleRepository.save(existing);
    } 

    //Delete rule
    public void deleteRule(Long id){
        ruleRepository.deleteById(id);
    }



}
