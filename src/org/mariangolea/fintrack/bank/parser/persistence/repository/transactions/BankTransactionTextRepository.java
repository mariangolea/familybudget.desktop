package org.mariangolea.fintrack.bank.parser.persistence.repository.transactions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankTransactionTextRepository  extends JpaRepository<BankTransactionText, Integer>{
    
}
