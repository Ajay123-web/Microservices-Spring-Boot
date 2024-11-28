package com.microservices.accounts.service;

import com.microservices.accounts.dto.CustomerDto;
import com.microservices.accounts.dto.ResponseDto;

public interface IAccountService {

    void createAccount(CustomerDto customerDto);
    CustomerDto getAccountDetails(String mobileNumber);

    boolean updateAccountDetails(CustomerDto customerDto);

    boolean deleteAccount(String mobileNumber);
}
