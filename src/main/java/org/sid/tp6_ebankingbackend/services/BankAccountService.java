package org.sid.tp6_ebankingbackend.services;

import org.sid.tp6_ebankingbackend.dtos.*;
import org.sid.tp6_ebankingbackend.entities.*;
import org.sid.tp6_ebankingbackend.entities.BankAccountDTO;
import org.sid.tp6_ebankingbackend.exceptions.BalanceNotSufficientException;
import org.sid.tp6_ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.tp6_ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer (CustomerDTO customerDTO);

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void  deleteCustomer(Long custumerId);

   CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

    List<CustomerDTO> listCustomers();
    //les exception que ca peut genéré
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId,double amount,String description) throws BankAccountNotFoundException, BalanceNotSufficientException, BalanceNotSufficientException;
    void credit(String accountId,double amount,String description) throws BankAccountNotFoundException;
    void transfer(String accountIdSource,String accountIdDestination ,double amount ) throws BankAccountNotFoundException, BalanceNotSufficientException;
   List<BankAccountDTO> bankAccountList();
    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException ;

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

    List<CustomerDTO> searchCustomers(String keyword);

    ;
}
