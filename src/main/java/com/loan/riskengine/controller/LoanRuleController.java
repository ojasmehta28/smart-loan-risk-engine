package com.loan.riskengine.controller;

import com.loan.riskengine.entity.LoanRuleEntity;
import com.loan.riskengine.service.LoanRuleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rules")
public class LoanRuleController {

    @Autowired
    private LoanRuleService ruleService; // perform operations related to loan rules.
    
    //Create
    @PostMapping
    public LoanRuleEntity addRule(@RequestBody LoanRuleEntity rule){
        return ruleService.addRule(rule);
    }

    //Read
    @GetMapping
    public List<LoanRuleEntity> getAllRules(){
        return ruleService.getAllRules();
    }

    //Update
    @PutMapping("/{id}")
    public LoanRuleEntity updateRule(@PathVariable Long id, @RequestBody LoanRuleEntity rule){ //pathvariable is used to extract the id from the URL, and requestbody is used to map the incoming JSON data to a LoanRuleEntity object.
        return ruleService.updateRule(id, rule);
    }

    //Delete
    @DeleteMapping("/{id}")
    public String deleteRule(@PathVariable Long id){
        ruleService.deleteRule(id);
        return "Rule deleted successfully";
    }
}
