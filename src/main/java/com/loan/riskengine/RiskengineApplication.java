package com.loan.riskengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication; // Entry point for the Spring Boot application

@SpringBootApplication // Enable auto-configuration and component scanning
public class RiskengineApplication { // Main application class to bootstrap the Spring Boot application

	public static void main(String[] args) {
		SpringApplication.run(RiskengineApplication.class, args); 
	}

}
