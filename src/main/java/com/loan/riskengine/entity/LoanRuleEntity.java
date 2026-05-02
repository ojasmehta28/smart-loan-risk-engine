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

    // Field name (income / creditScore)
    private String field;

    // Operator (>, <, >=, <=, ==)
    private String operator;

    // Value to compare against
    private double value;

    // Decision result
    private String decision;

    // Execution priority (lower = higher priority)
    private int priority;

    // One rule can have multiple conditions (for complex rules)
    @OneToMany(mappedBy = "rule", cascade = CascadeType.ALL)
    private List<RuleCondition> conditions;

    private String logicalOperator; // AND / OR
}