package com.loan.riskengine.dto; //dto package for data transfer objects in the risk engine module 
import jakarta.validation.constraints.*; // Import validation annotations 
import lombok.*; // Import Lombok annotations 

@Data // Lombok annotation to generate getters & setters
@NoArgsConstructor // Lombok annotation to generate a no-args constructor
@AllArgsConstructor // Lombok annotation to generate an all-args constructor
public class LoanRequestDTO {

    @NotBlank(message= "Applicant name is required") // Validation annotation to ensure the field is not blank
    private String applicantName;

    @Min(value=1, message = "Income must be greater than 0") // Validation annotation to ensure the value is at least 1
    private double Income;

    @Min(value=300, message="Credit Score must be at least 300") // Validation annotation to ensure the value is at least 300
    @Max(value=900, message="Credit Score must be at most 850") // Validation annotation to ensure the value is at most 850
    private int creditScore;

    @Min(value=1, message = "Loan amount must be greater than 0") // Validation annotation to ensure the value is at least 1
    private double loanAmount;

    
}
