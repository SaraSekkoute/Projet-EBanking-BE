package org.sid.tp6_ebankingbackend.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.tp6_ebankingbackend.entities.AccountOperation;
import org.sid.tp6_ebankingbackend.entities.BankAccountDTO;
import org.sid.tp6_ebankingbackend.entities.Customer;
import org.sid.tp6_ebankingbackend.entities.enums.AccountStatus;

import java.util.Date;
import java.util.List;



@Data
public class CurrentBankAccountDTO  extends BankAccountDTO  {

    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerdto;
    private double overDraft;

    //==>mappers
}
