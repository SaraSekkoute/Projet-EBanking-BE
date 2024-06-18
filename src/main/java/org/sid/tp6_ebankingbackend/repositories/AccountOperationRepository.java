package org.sid.tp6_ebankingbackend.repositories;

import org.sid.tp6_ebankingbackend.entities.AccountOperation;
import org.sid.tp6_ebankingbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
}
