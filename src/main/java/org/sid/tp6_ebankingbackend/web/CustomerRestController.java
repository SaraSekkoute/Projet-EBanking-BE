package org.sid.tp6_ebankingbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.tp6_ebankingbackend.dtos.CustomerDTO;
import org.sid.tp6_ebankingbackend.entities.Customer;
import org.sid.tp6_ebankingbackend.exceptions.CustomerNotFoundException;
import org.sid.tp6_ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerRestController {
    private BankAccountService bankAccountService;
    @GetMapping("/customers")
    public List<CustomerDTO> customers()
    {
        return bankAccountService.listCustomers();
    }
    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable(name ="id") Long customerID) throws CustomerNotFoundException {
                 return  bankAccountService.getCustomer(customerID);
    }
    @PostMapping("/customers")
    public CustomerDTO saveCustomer (@RequestBody CustomerDTO customerDTO)
    {
       return bankAccountService.saveCustomer(customerDTO);
    }
    @PutMapping("/customer/{customerId}")
    public  CustomerDTO updateCustomer(@PathVariable Long customerId,@RequestBody CustomerDTO customerDTO)
    {
        customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerDTO);
    }
@DeleteMapping("/customer/{id}")
    public   void deleteCustomer(@PathVariable Long id)
    {
       bankAccountService.deleteCustomer(id);
    }





}
