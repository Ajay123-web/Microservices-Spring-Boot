package com.microservices.accounts.service.impl;

import com.microservices.accounts.constants.AccountsConstants;
import com.microservices.accounts.dto.AccountsDto;
import com.microservices.accounts.dto.CustomerDto;
import com.microservices.accounts.entity.Accounts;
import com.microservices.accounts.entity.Customer;
import com.microservices.accounts.exception.AccountAlreadyExistsException;
import com.microservices.accounts.exception.CustomerAlreadyExistsException;
import com.microservices.accounts.exception.ResourceNotFoundException;
import com.microservices.accounts.mapper.AccountsMapper;
import com.microservices.accounts.mapper.CustomerMapper;
import com.microservices.accounts.repository.AccountsRepository;
import com.microservices.accounts.repository.CustomerRepository;
import com.microservices.accounts.service.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountService {
    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;
    @Override
    @Transactional
    public void createAccount(CustomerDto customerDto) {
        Customer savedCustomer = null;
        try{
            Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());

            savedCustomer = customerRepository.save(customer);
            accountsRepository.save(createNewAccount(savedCustomer));
        } catch(DataIntegrityViolationException e) {
            System.out.println("Record Exists ERROR!! " + e.getMessage());
            /*
             TODO: Not working properly
             */
            if(savedCustomer == null) throw new CustomerAlreadyExistsException(e.getMessage());

            throw new AccountAlreadyExistsException(e.getMessage());
        } catch(Exception e) {
            System.out.println("Not able to save ERROR!! " + e.getMessage());
            throw e;
        }

    }

    @Override
    public CustomerDto getAccountDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "Mobile Number", mobileNumber)
        );

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Accounts", "Customer Id", customer.getCustomerId().toString())
        );

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        return customerDto;
    }

    @Override
    @Transactional
    public boolean updateAccountDetails(CustomerDto customerDto) {
        boolean isUpdated = false;
        try {
            if(customerDto.getAccountsDto() != null && customerDto.getAccountsDto().getAccountNumber() != null) {
                Accounts accounts = accountsRepository.findById(customerDto.getAccountsDto().getAccountNumber()).orElseThrow(
                        () -> new ResourceNotFoundException("Accounts", "Account Number", String.valueOf(customerDto.getAccountsDto().getAccountNumber()))
                );
                AccountsMapper.mapToAccounts(customerDto.getAccountsDto(), accounts);
                accountsRepository.save(accounts);

                Customer customer = customerRepository.findById(accounts.getCustomerId()).orElseThrow(
                        () -> new ResourceNotFoundException("Customer", "Customer Id", accounts.getCustomerId().toString())
                );
                CustomerMapper.mapToCustomer(customerDto, customer);
                customerRepository.save(customer);
                isUpdated = true;
            }

        } catch (DataIntegrityViolationException e) {
            System.out.println("Record Exists ERROR!! " + e.getMessage());
            throw new CustomerAlreadyExistsException(e.getMessage());
        } catch(Exception e) {
            System.out.println("Not able to update ERROR!! " + e.getMessage());
            throw e;
        }

        return isUpdated;
    }

    @Override
    @Transactional
    public boolean deleteAccount(String mobileNumber) {
        boolean isDeleted = false;
        try {
            Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "Mobile Number", mobileNumber)
            );
            customerRepository.deleteById(customer.getCustomerId());
            accountsRepository.deleteByCustomerId(customer.getCustomerId());

            isDeleted = true;
        } catch(Exception e) {
            System.out.println("Not able to delete ERROR!! " + e.getMessage());
            throw e;
        }

        return isDeleted;
    }

    private Accounts createNewAccount(Customer savedCustomer) {
        Accounts accounts = new Accounts();
        accounts.setCustomerId(savedCustomer.getCustomerId());
        accounts.setAccountNumber(1000000000L + new Random().nextInt(900000000));
        accounts.setAccountType(AccountsConstants.SAVINGS);
        accounts.setBranchAddress(AccountsConstants.ADDRESS);
        accounts.setCreatedAt(LocalDateTime.now());
        accounts.setCreatedBy(savedCustomer.getCreatedBy());
        return accounts;
    }
}
