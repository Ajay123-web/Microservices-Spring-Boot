package com.microservices.accounts.exception;

import com.microservices.accounts.constants.AccountsConstants;
import com.microservices.accounts.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(request.getDescription(false), ex.getMessage(), AccountsConstants.INVALID_ARG, LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDto, ex.getStatusCode());

        /*
            TODO: Write logic for fetching all validations which failed
         */
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<?> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException ex, WebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(request.getDescription(false), ex.getMessage(), AccountsConstants.CUSTOMER_EXISTS, LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDto, ex.getStatusCode());
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<?> handleAccountAlreadyExistsException(AccountAlreadyExistsException ex, WebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(request.getDescription(false), ex.getMessage(), AccountsConstants.ACCOUNT_EXISTS, LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDto, ex.getStatusCode());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(request.getDescription(false), ex.getMessage(), AccountsConstants.RESOURCE_DOES_NOT_EXISTS, LocalDateTime.now());
        return new ResponseEntity<>(errorResponseDto, ex.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception exception,
                                                                  WebRequest webRequest) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                exception.getMessage(),
                "5xx",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
