package com.loan.riskengine.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RuleCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String field;     // income / creditScore
    private String operator;  // >, <, >=, <=
    private double value;

    // Many conditions belong to one rule
    @ManyToOne
    @JoinColumn(name = "rule_id")
    private LoanRuleEntity rule;
}