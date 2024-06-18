package org.sid.tp6_ebankingbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.tp6_ebankingbackend.entities.enums.AccountStatus;

import java.util.Date;
import java.util.List;
@Entity
//SINGLE_TABLE
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE",length = 4,discriminatorType = DiscriminatorType.STRING)

//TABLE_PER_CLASS(je n'aurais jamais besoin de créer table bankaccount =>public obstract BankAccount {)
//@Inheritance(strategy =InheritanceType.TABLE_PER_CLASS)

//JOINED
//@Inheritance(strategy = InheritanceType.JOINED)


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {
    @Id
    private String id;
    private double balance;
    private Date createdAt;

@Enumerated(EnumType.STRING)
    private AccountStatus status;
    //plusieurs customar pour un client
@ManyToOne
private Customer customer;

//lazy (par defaut)on ne va charger les données que en cas de besoin
@OneToMany (mappedBy = "bankAccount",fetch =FetchType.LAZY)
   private List<AccountOperation> accountOperations;
}
