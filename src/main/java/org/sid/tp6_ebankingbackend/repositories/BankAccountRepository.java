package org.sid.tp6_ebankingbackend.repositories;

import org.sid.tp6_ebankingbackend.entities.BankAccount;
import org.sid.tp6_ebankingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
}
