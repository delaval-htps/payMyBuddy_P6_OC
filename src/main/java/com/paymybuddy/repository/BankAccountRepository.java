package com.paymybuddy.repository;

import java.util.Optional;
import com.paymybuddy.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository

public interface BankAccountRepository extends JpaRepository<BankAccount,Long>{

    @Query(value = "select ba from BankAccount ba where ba=?1")
    Optional<BankAccount> findByBankAccount(BankAccount bankAccount);

   
    
}
