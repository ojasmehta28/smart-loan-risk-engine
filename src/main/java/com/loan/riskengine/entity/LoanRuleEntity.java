package com.loan.riskengine.entity;

import jakarta.persistence.*;
import lombok.*;
// import java.util.List; // used for condition-based rules

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LoanRuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Decision result (APPROVED / REJECTED)
    private String decision;

    // Priority (lower number = higher priority)
    private int priority;

    
    // private String logicalOperator; // "AND" or "OR" (used for condition-based rules)

    
    // @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL) 
    // private List<RuleCondition> conditions; // List of conditions (used for condition-based rules)

    
    private String expression; // e.g., "income > 50000 AND creditScore > 700" (used for expression-based rules)
}