package com.loan.riskengine.repository;

import com.loan.riskengine.entity.LoanRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRuleRepository extends JpaRepository<LoanRuleEntity, Long> {
}