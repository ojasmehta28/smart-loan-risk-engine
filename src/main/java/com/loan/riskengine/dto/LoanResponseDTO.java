package com.loan.riskengine.dto;

import lombok.*; // Import Lombok annotations 

@Data // Lombok annotation to generate getters & setters
@NoArgsConstructor // Lombok annotation to generate a no-args constructor
@AllArgsConstructor // Lombok annotation to generate an all-args constructor
public class LoanResponseDTO {
    private long id;
    private String applicantName;
    private double Income;
    private int creditScore;
    private double loanAmount;
    private String status;
    

}
