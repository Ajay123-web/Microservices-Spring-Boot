package com.microservices.accounts.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String entity, String field, String fieldVal) {
        super(entity + " with " + field + " " + fieldVal + " not found");
    }

    private final HttpStatus statusCode = HttpStatus.BAD_REQUEST;

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
