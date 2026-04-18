package com.loan.riskengine.repository;

import com.loan.riskengine.entity.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<LoanApplication, Long> {
}