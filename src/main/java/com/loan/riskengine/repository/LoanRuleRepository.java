package com.loan.riskengine.repository;

import com.loan.riskengine.entity.LoanRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository; // jpa repository generally used for database operations on entities, providing CRUD methods out of the box
import java.util.List;

public interface LoanRuleRepository extends JpaRepository<LoanRuleEntity, Long> { //interface for accessing loan rules from the database, extending JpaRepository for CRUD operations

    // Fetch all rules ordered by priority (ascending)
    // Meaning: priority = 1 will come first, then 2, then 3...
    List<LoanRuleEntity> findAllByOrderByPriorityAsc(); // Custom query method to fetch all loan rules ordered by their priority in ascending order
}