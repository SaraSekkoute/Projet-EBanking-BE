package org.sid.tp6_ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.tp6_ebankingbackend.dtos.*;
import org.sid.tp6_ebankingbackend.entities.*;
import org.sid.tp6_ebankingbackend.entities.BankAccountDTO;
import org.sid.tp6_ebankingbackend.entities.enums.OperationType;
import org.sid.tp6_ebankingbackend.exceptions.BalanceNotSufficientException;
import org.sid.tp6_ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.tp6_ebankingbackend.exceptions.CustomerNotFoundException;
import org.sid.tp6_ebankingbackend.mappers.BankAccountMapperImpl;
import org.sid.tp6_ebankingbackend.repositories.AccountOperationRepository;
import org.sid.tp6_ebankingbackend.repositories.BankAccountRepository;
import org.sid.tp6_ebankingbackend.repositories.CustomerRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;



@Service
@Transactional
@AllArgsConstructor
//for logger
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{
//   @Autowired
    private CustomerRepository customerRepository;
//    @Autowired
    private BankAccountRepository bankAccountRepository;
//    @Autowired
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;

   // Logger log = LoggerFactory.getLogger(this.getClass().getName());
    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        //transfert dto vers entities
        Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
        Customer savecustomer=customerRepository.save(customer);
        return dtoMapper.fromCustomer(savecustomer);
    }


    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        //transfert dto vers entities
        Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
        Customer savecustomer=customerRepository.save(customer);
        return dtoMapper.fromCustomer(savecustomer);
    }

    @Override
    public void  deleteCustomer(Long custumerId)
    {
        customerRepository.deleteById(custumerId);

    }





    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {


        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer == null)
            throw new CustomerNotFoundException("Customer not found");


        CurrentAccount currentAccount=new CurrentAccount();



        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
      CurrentAccount savedBankAccount= bankAccountRepository.save(currentAccount);
      //i.e. transfer from currentbankAccount to currentbankAccountdto
      return dtoMapper.fromCurrentBankAccount(savedBankAccount);

    }


//    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
//
//        Customer customer=customerRepository.findById(customerId).orElse(null);
//        if(customer == null)
//            throw new CustomerNotFoundException("Customer not found");
//
//
//       SavingAccount savingAccount=new SavingAccount();
//
//
//
//        savingAccount.setId(UUID.randomUUID().toString());
//        savingAccount.setCreatedAt(new Date());
//        savingAccount.setBalance(initialBalance);
//        savingAccount.setInterestRate(interestRate);
//        savingAccount.setCustomer(customer);
//        SavingAccount savedBankAccount= bankAccountRepository.save(savingAccount);
//        return savedBankAccount;
//    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {

        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer == null)
            throw new CustomerNotFoundException("Customer not found");


        SavingAccount savingAccount=new SavingAccount();



        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedBankAccount= bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingBankAccount(savedBankAccount);
    }

    //customer dto to customer
    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers= customerRepository.findAll();
//    programmation imperatif classic    List<CustomerDTO> customerDTOS= new ArrayList<>();
//        for(Customer customer:customers)
//        {
//            CustomerDTO customerDTO=dtoMapper.fromCustomer(customer);
//            customerDTOS.add(customerDTO);
//        }prg fonctionnel
        List<CustomerDTO> customerDTOS= customers.stream()
                .map(cust->dtoMapper.fromCustomer(cust))
                .collect(Collectors.toList());
        return customerDTOS;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElseThrow(
                ()->new BankAccountNotFoundException("BankAccount not found")
        );
        if(bankAccount instanceof SavingAccount)
        {
            SavingAccount savingAccount=(SavingAccount) bankAccount;
            return dtoMapper.fromSavingBankAccount(savingAccount);
        }
        else
        {
            CurrentAccount currentAccount=(CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentBankAccount(currentAccount);
        }

    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElseThrow(
                ()->new BankAccountNotFoundException("BankAccount not found")
        );
        if(bankAccount.getBalance()<amount)
         throw new BalanceNotSufficientException("Balance not sufficient");
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElseThrow(
                ()->new BankAccountNotFoundException("BankAccount not found")
        );

        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource,amount,"Transfer to "+ accountIdDestination);
        credit(accountIdSource,amount,"Transfer from "+accountIdSource);
    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
       List<BankAccount> bankAccounts =bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountsDTO = bankAccounts.stream().map(bankAccount -> {
           if(bankAccount instanceof SavingAccount)
           {
               SavingAccount savingAccount=(SavingAccount) bankAccount;
               return dtoMapper.fromSavingBankAccount(savingAccount);
           }
           else
           {
               CurrentAccount currentAccount=(CurrentAccount) bankAccount;
               return dtoMapper.fromCurrentBankAccount(currentAccount);
           }


       }
       ).collect(Collectors.toList());

        return bankAccountsDTO;
    }
    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerId).orElseThrow(
                ()->new CustomerNotFoundException("Customer Not found")
        );
        return  dtoMapper.fromCustomer(customer);
    }
    @Override
public List<AccountOperationDTO> accountHistory(String accountId)
    {
        List<AccountOperation > accountOperations=accountOperationRepository.findByBankAccount_Id(accountId);
        return accountOperations.stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());

    }
    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElse(null);
        if(bankAccount == null) throw new BankAccountNotFoundException("Account not Found");
Page<AccountOperation> accountOperations= accountOperationRepository.findByBankAccount_Id(accountId, PageRequest.of(page,size));
 AccountHistoryDTO accountHistoryDTO=new AccountHistoryDTO();





        List<AccountOperationDTO>  accountOperationDTOS = accountOperations.getContent().stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
 accountHistoryDTO.setAccountOperationDTOList(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setTotalPages(size);
        accountHistoryDTO.setTotalPages(accountHistoryDTO.getTotalPages());

 return  accountHistoryDTO;
    }





//==>web
}
