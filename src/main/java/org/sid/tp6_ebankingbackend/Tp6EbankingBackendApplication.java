package org.sid.tp6_ebankingbackend;

import org.sid.tp6_ebankingbackend.entities.*;
import org.sid.tp6_ebankingbackend.entities.enums.AccountStatus;
import org.sid.tp6_ebankingbackend.entities.enums.OperationType;
import org.sid.tp6_ebankingbackend.repositories.AccountOperationRepository;
import org.sid.tp6_ebankingbackend.repositories.BankAccountRepository;
import org.sid.tp6_ebankingbackend.repositories.CustomerRepository;
import org.sid.tp6_ebankingbackend.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class Tp6EbankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(Tp6EbankingBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(
//										BankAccountRepository bankAccountRepository
			BankService bankService)
	{


		return arg->
				{
					bankService.consulter();
//					BankAccount bankAccount = bankAccountRepository.findById("293b22ea-a601-4fa5-997d-a6c95a5ef5ad").orElse(null);
//
//			if (bankAccount != null) {
//				System.out.println("****************************************************");
//				System.out.println(bankAccount.getId());
//				System.out.println(bankAccount.getStatus());
//				System.out.println(bankAccount.getBalance());
//				System.out.println(bankAccount.getCreatedAt());
//				System.out.println(bankAccount.getCustomer().getName());
//
//				//la classse de ce account
//				System.out.println(bankAccount.getClass().getSimpleName());
//
//				if (bankAccount instanceof CurrentAccount) {
//					System.out.println("Over Draft =>" + ((CurrentAccount) bankAccount).getOverDraft());
//
//				} else if (bankAccount instanceof SavingAccount) {
//					System.out.println("Rate =>" + ((SavingAccount) bankAccount).getInterestRate());
//				}
//
//
//				bankAccount.getAccountOperations().forEach(
//						op -> {
//							System.out.println(op.getType() + "/t" + op.getOperationDate() + "/t" + op.getAmount());
//						}
//				);
//			}

		};
	}
	//@Bean
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
