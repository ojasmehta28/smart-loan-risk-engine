package com.loan.riskengine.entity;

import jakarta.persistence.*;
import lombok.*;

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
}