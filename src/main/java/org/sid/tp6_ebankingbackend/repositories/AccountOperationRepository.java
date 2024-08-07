package org.sid.tp6_ebankingbackend.repositories;

import org.sid.tp6_ebankingbackend.entities.AccountOperation;
import org.sid.tp6_ebankingbackend.entities.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
   List<AccountOperation> findByBankAccount_Id(String accountId);
   Page<AccountOperation> findByBankAccountIdOrderByOperationDateDesc(String accountId, Pageable pageable);

}
