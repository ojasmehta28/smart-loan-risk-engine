package com.loan.riskengine.entity;

import jakarta.persistence.*; // Import JPA annotations
import lombok.*; // Import Lombok annotations

@Data // Lombok annotation to generate getters & setters
@NoArgsConstructor // Lombok annotation to generate a no-args constructor
@AllArgsConstructor // Lombok annotation to generate an all-args constructor
@Entity // JPA annotation to mark this class as a database entity
public class LoanRuleEntity {

    @Id // Primary key for the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate ID values
    private long id;

    private double minIncome; // Minimum income required for the loan;
    private int minCreditScore; // Minimum credit score required for the loan;
    private String decision; // Decision based on the rule, e.g., "APPROVE", "REJECT"
    private int priority; // Priority of the rule (lower number = higher priority)



}
