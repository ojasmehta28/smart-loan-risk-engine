package com.loan.riskengine.entity;

import jakarta.persistence.*;

@Entity
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String applicantName;

    private double income;

    private int creditScore;

    private double loanAmount;

    private String status;
}