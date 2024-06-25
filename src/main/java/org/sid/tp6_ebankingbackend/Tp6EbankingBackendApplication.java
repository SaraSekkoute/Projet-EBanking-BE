package org.sid.tp6_ebankingbackend;

import org.sid.tp6_ebankingbackend.dtos.CurrentBankAccountDTO;
import org.sid.tp6_ebankingbackend.dtos.CustomerDTO;
import org.sid.tp6_ebankingbackend.dtos.SavingBankAccountDTO;
import org.sid.tp6_ebankingbackend.entities.*;
import org.sid.tp6_ebankingbackend.entities.BankAccountDTO;
import org.sid.tp6_ebankingbackend.entities.enums.AccountStatus;
import org.sid.tp6_ebankingbackend.entities.enums.OperationType;
import org.sid.tp6_ebankingbackend.exceptions.BalanceNotSufficientException;
import org.sid.tp6_ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.tp6_ebankingbackend.exceptions.CustomerNotFoundException;
import org.sid.tp6_ebankingbackend.repositories.AccountOperationRepository;
import org.sid.tp6_ebankingbackend.repositories.BankAccountRepository;
import org.sid.tp6_ebankingbackend.repositories.CustomerRepository;
import org.sid.tp6_ebankingbackend.services.BankAccountService;
import org.sid.tp6_ebankingbackend.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class Tp6EbankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(Tp6EbankingBackendApplication.class, args);
	}

	//@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService)
	{


		return arg->
				{
					Stream.of("Sara","Imane","Hassan").forEach(
							name-> {
								CustomerDTO customer = new CustomerDTO();
								customer.setName(name);
								customer.setEmail(name+"@gmail.com");
								bankAccountService.saveCustomer(customer);
							});
//for each customer

					bankAccountService.listCustomers().forEach(customer ->
					{

                        try {
                            bankAccountService.saveCurrentBankAccount(Math.random()*90000,90000, customer.getId());
							bankAccountService.saveSavingBankAccount(Math.random()*120000,5.5, customer.getId());


                        } catch (CustomerNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
					List<BankAccountDTO> bankAccounts=bankAccountService.bankAccountList();
					for (BankAccountDTO bankAccount:bankAccounts)
					{
						for (int i=0;i<10;i++)
						{
							String accountId;
							if(bankAccount instanceof SavingBankAccountDTO)
							{
								accountId=((SavingBankAccountDTO) bankAccount).getId();
							}
							else
							{
								accountId=((CurrentBankAccountDTO) bankAccount).getId();


							}
							bankAccountService.credit(accountId,1000+Math.random()*120000,"Credit");
							bankAccountService.debit(accountId,1000+Math.random()*9000,"Debit");

						}
					}
		};
	}

























	//@Bean --  just for test
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository)
	{
		return args -> {
			Stream.of("Hassan","Yassine","Aicha").forEach(name->{
				Customer customer=new Customer();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
				customerRepository.save(customer);
			});
			//for each customer
			customerRepository.findAll().forEach(
					customer -> {

						//current account
						CurrentAccount currentAccount=new CurrentAccount();
						currentAccount.setId(UUID.randomUUID().toString());
						currentAccount.setBalance(Math.random()*90000);
						currentAccount.setCreatedAt(new Date());
						currentAccount.setStatus(AccountStatus.CREATED);
						currentAccount.setCustomer(customer);
						currentAccount.setOverDraft(90000);
						bankAccountRepository.save(currentAccount);

						//saving account
						SavingAccount savingAccount=new SavingAccount();
						savingAccount.setId(UUID.randomUUID().toString());
						savingAccount.setBalance(Math.random()*90000);
						savingAccount.setCreatedAt(new Date());
						savingAccount.setStatus(AccountStatus.CREATED);
						savingAccount.setCustomer(customer);
						savingAccount.setInterestRate(5);
						bankAccountRepository.save(savingAccount);





					}
			);
			bankAccountRepository.findAll().forEach(
					bkacc ->{
						for (int i=0;i<5;i++)
						{
							AccountOperation accountOperation=new AccountOperation();
							accountOperation.setOperationDate(new Date());
							accountOperation.setAmount(Math.random()*12000);
							accountOperation.setType(Math.random()>0.5? OperationType.DEBIT:OperationType.CREDIT);
							accountOperation.setBankAccount(bkacc);
							accountOperationRepository.save(accountOperation);

						}




					}
			);

		};

	}

}
