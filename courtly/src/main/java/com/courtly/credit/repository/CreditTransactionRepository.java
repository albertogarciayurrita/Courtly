package com.courtly.credit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.courtly.credit.entity.CreditTransaction;

public interface CreditTransactionRepository extends JpaRepository<CreditTransaction, Long>{
    
}
