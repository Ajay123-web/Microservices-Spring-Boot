package com.microservices.accounts.exception;

import org.springframework.http.HttpStatus;
public class AccountAlreadyExistsException extends RuntimeException{
    public AccountAlreadyExistsException(String message) {
        super(message);
    }

    private final HttpStatus statusCode = HttpStatus.BAD_REQUEST;

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
