package org.sid.tp6_ebankingbackend.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.tp6_ebankingbackend.entities.BankAccount;
import org.sid.tp6_ebankingbackend.entities.enums.OperationType;

import java.util.Date;


@Data
public class AccountOperationDTO {

    private Long id;
    private Date operationDate;
    private double amount;

    private OperationType type;
    private BankAccount bankAccount;
    private  String description ;

}
