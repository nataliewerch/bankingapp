package org.example.com.service.handler;

import org.example.com.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity clientNotFoundException(ClientNotFoundException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler
    public ResponseEntity accountNotFoundException(AccountNotFoundException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler
    public ResponseEntity insufficientBalanceException(InsufficientBalanceException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler
    public ResponseEntity agreementNotFoundException(AgreementNotFoundException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler
    public ResponseEntity managerNotFoundException(ManagerNotFoundException exception, HttpServletRequest request) {
        return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
