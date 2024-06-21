package org.sid.tp6_ebankingbackend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Customer {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private String email;

    @OneToMany(mappedBy = "customer" )

//You don’t necessarily need to consult the list of accounts(that is api that makes the serialize json (jakson library that converts object java on json) it is not worth serializing this attributes (bankAccountList) ignore the reading )
    @JsonProperty(access =JsonProperty.Access.WRITE_ONLY )
    private List<BankAccount> bankAccountList;

}
