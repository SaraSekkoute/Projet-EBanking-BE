package org.sid.tp6_ebankingbackend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
//SINGLE_TABLE
@DiscriminatorValue("SA")
//TABLE_PER_CLASS

//JOINED

@Data @NoArgsConstructor @AllArgsConstructor
public class SavingAccount extends BankAccount{
    private  double interestRate;
}
