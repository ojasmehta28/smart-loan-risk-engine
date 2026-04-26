package com.loan.riskengine.ruleengine;

import com.loan.riskengine.entity.LoanRuleEntity;
import com.loan.riskengine.repository.LoanRuleRepository;
import com.loan.riskengine.entity.LoanApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoanRuleEngine {

    // Inject repository to fetch rules from DB
    @Autowired
    private LoanRuleRepository ruleRepository;

    public String evaluate(LoanApplication loan) {

        // STEP 1: Fetch rules from DB in priority order
        // Rules are sorted: priority 1 → 2 → 3...
        List<LoanRuleEntity> rules = ruleRepository.findAllByOrderByPriorityAsc();

        // STEP 2: Evaluate rules one by one

        for (LoanRuleEntity rule : rules) {

            // Check if loan satisfies current rule condition
            if (loan.getIncome() >= rule.getMinIncome() &&
                loan.getCreditScore() >= rule.getMinCreditScore()) {

                // STEP 3: Return decision immediately

                return rule.getDecision();
            }
        }

        
        // STEP 4: Default fallback
        

        // If no rule matched → send to manual review
        return "REVIEW";
    }
}