package org.sid.tp6_ebankingbackend.mappers;

import com.fasterxml.jackson.databind.util.BeanUtil;
import org.sid.tp6_ebankingbackend.dtos.AccountOperationDTO;
import org.sid.tp6_ebankingbackend.dtos.CurrentBankAccountDTO;
import org.sid.tp6_ebankingbackend.dtos.CustomerDTO;
import org.sid.tp6_ebankingbackend.dtos.SavingBankAccountDTO;
import org.sid.tp6_ebankingbackend.entities.AccountOperation;
import org.sid.tp6_ebankingbackend.entities.CurrentAccount;
import org.sid.tp6_ebankingbackend.entities.Customer;
import org.sid.tp6_ebankingbackend.entities.SavingAccount;
import org.sid.tp6_ebankingbackend.repositories.CustomerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {

    public CustomerDTO fromCustomer(Customer customer)
    {
        CustomerDTO customerDTO= new  CustomerDTO();

//        customerDTO.setId(customer.getId());
//        customerDTO.setName(customerDTO.getName());
//        customerDTO.setEmail(customerDTO.getEmail());
        BeanUtils.copyProperties(customer,customerDTO);
        return  customerDTO;
    }
    //from
    public Customer fromCustomerDTO (CustomerDTO customerDTO)
    {
        Customer customer= new  Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return  customer;
    }


    public SavingBankAccountDTO fromSavingBankAccount(SavingAccount savingAccount)
    {
        //Transfer
        SavingBankAccountDTO savingBankAccountDTO =new SavingBankAccountDTO();
        BeanUtils.copyProperties(savingAccount,savingBankAccountDTO);
        savingBankAccountDTO.setCustomerdto(fromCustomer(savingAccount.getCustomer()));
        //recupperer le nom de l'objet
        savingBankAccountDTO.setType(savingAccount.getClass().getSimpleName());
        return  savingBankAccountDTO;

    }


    public SavingAccount fromSavingBankAccountDTO(SavingBankAccountDTO savingBankAccountDTO)
    {
        SavingAccount savingAccount =new SavingAccount();
        BeanUtils.copyProperties(savingBankAccountDTO,savingAccount);
        savingAccount.setCustomer(fromCustomerDTO(savingBankAccountDTO.getCustomerdto()));

        return  savingAccount;
    }


    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount currentAccount)
    {
        CurrentBankAccountDTO currentBankAccountDTO =new CurrentBankAccountDTO();
        BeanUtils.copyProperties(currentAccount,currentBankAccountDTO);
       currentBankAccountDTO.setCustomerdto(fromCustomer(currentAccount.getCustomer()));
        currentBankAccountDTO.setType(currentAccount.getClass().getSimpleName());

        return  currentBankAccountDTO;
    }


    public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO currentBankAccountDTO)
    {
        CurrentAccount currentAccount =new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDTO,currentAccount);
        currentAccount.setCustomer(fromCustomerDTO(currentBankAccountDTO.getCustomerdto()));
        return  currentAccount;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation)
    {
        AccountOperationDTO accountOperationDTO=new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation,accountOperationDTO);

        return  accountOperationDTO;
    }



    //->service

}
