package org.sid.tp6_ebankingbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.tp6_ebankingbackend.entities.enums.AccountStatus;

import java.util.Date;
import java.util.List;


@Data
public class BankAccountDTO {
//    @Id
   private String type;
//    private double balance;
//    private Date createdAt;
//
//    @Enumerated(EnumType.STRING)
//    private AccountStatus status;
//    //plusieurs customar pour un client
//    @ManyToOne
//    private Customer customer;
//
//    //lazy (par defaut)on ne va charger les données que en cas de besoin
//    @OneToMany (mappedBy = "bankAccount",fetch =FetchType.LAZY)
//    private List<AccountOperation> accountOperations;
}
