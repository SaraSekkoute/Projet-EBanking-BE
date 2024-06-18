package org.sid.tp6_ebankingbackend.services;

import jakarta.transaction.Transactional;
import org.sid.tp6_ebankingbackend.entities.BankAccount;
import org.sid.tp6_ebankingbackend.entities.CurrentAccount;
import org.sid.tp6_ebankingbackend.entities.SavingAccount;
import org.sid.tp6_ebankingbackend.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    public void consulter()
    {
        BankAccount bankAccount = bankAccountRepository.findById("293b22ea-a601-4fa5-997d-a6c95a5ef5ad").orElse(null);

        if (bankAccount != null) {
            System.out.println("****************************************************");
            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getStatus());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getCreatedAt());
            System.out.println(bankAccount.getCustomer().getName());

            //la classse de ce account
            System.out.println(bankAccount.getClass().getSimpleName());

            if (bankAccount instanceof CurrentAccount) {
                System.out.println("Over Draft =>" + ((CurrentAccount) bankAccount).getOverDraft());

            } else if (bankAccount instanceof SavingAccount) {
                System.out.println("Rate =>" + ((SavingAccount) bankAccount).getInterestRate());
            }


            bankAccount.getAccountOperations().forEach(
                    op -> {
                        System.out.println(op.getType() + "/t" + op.getOperationDate() + "/t" + op.getAmount());
                    }
            );
        }
    }
}
