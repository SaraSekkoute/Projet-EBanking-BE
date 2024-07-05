package org.sid.tp6_ebankingbackend.web;

import lombok.AllArgsConstructor;
import org.sid.tp6_ebankingbackend.dtos.*;
import org.sid.tp6_ebankingbackend.entities.BankAccountDTO;
import org.sid.tp6_ebankingbackend.exceptions.BalanceNotSufficientException;
import org.sid.tp6_ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.tp6_ebankingbackend.services.BankAccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor

@CrossOrigin("*")
public class BankAccountRestAPI {

    private BankAccountService bankAccountService;
@GetMapping("/accounts/{accountId}")
@PreAuthorize("hasAnyAuthority('SCOPE_USER')")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);

    }


    @GetMapping("/accounts")
    @PreAuthorize("hasAnyAuthority('SCOPE_USER')")
    public List<BankAccountDTO> ListAccounts() {
        return bankAccountService.bankAccountList();

    }
    @GetMapping("/accounts/{accountId}/operations")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId) {
        return bankAccountService.accountHistory(accountId);

    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    @PreAuthorize("hasAnyAuthority('SCOPE_USER')")
    public AccountHistoryDTO getHistory(@PathVariable String accountId ,
                                              @RequestParam( name="page" ,defaultValue = "0") int page
                                            , @RequestParam( name="size" ,defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId,page,size);

    }
@PostMapping("/accounts/debit")
@PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public DebitDTO debit( @RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.debit(debitDTO.getAccountId(),debitDTO.getAmount(),debitDTO.getDescription());
        return debitDTO;
    }


    @PostMapping("/accounts/credit")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public CreditDTO credit( @RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException {
        this.bankAccountService.credit(creditDTO.getAccountId(),creditDTO.getAmount(),creditDTO.getDescription());
        return creditDTO;
    }
    @PostMapping("/accounts/transfer")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.transfer(transferRequestDTO.getAccountSource(),transferRequestDTO.getAccountDestination(),
                transferRequestDTO.getAmount());

    }



}
