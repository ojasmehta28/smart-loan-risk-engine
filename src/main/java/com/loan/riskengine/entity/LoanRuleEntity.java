package com.loan.riskengine.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

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

    // AND / OR logic
    private String logicalOperator;

    // One rule can have multiple conditions
    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL)
    private List<RuleCondition> conditions;
}