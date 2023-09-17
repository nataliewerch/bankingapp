package org.example.com.service.handler;

import org.example.com.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Exception handler for controller-level exceptions.
 *
 * @author Natalie Werch
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Handle exceptions related to entity not found.
     *
     * @param exception - The exception to be handled.
     * @param request - The HTTP request associated with the exception.
     * @return A ResponseEntity with a status of NOT_FOUND and an error message.
     */
    @ExceptionHandler({ClientNotFoundException.class,
            AccountNotFoundException.class,
            AgreementNotFoundException.class,
            ManagerNotFoundException.class,
            ProductNotFoundException.class,
            TransactionNotFoundException.class})
    public ResponseEntity<String> entityNotFoundException(Exception exception, HttpServletRequest request) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handle exceptions related to forbidden access.
     *
     * @param exception - The exception to be handled.
     * @param request - The HTTP request associated with the exception.
     * @return A ResponseEntity with a status of FORBIDDEN and an error message.
     */
    @ExceptionHandler({InsufficientBalanceException.class,
            AccessDeniedException.class})
    public ResponseEntity<String> forbiddenException(Exception exception, HttpServletRequest request) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    /**
     * Handle exceptions related to conflicts.
     *
     * @param exception - The exception to be handled.
     * @param request - The HTTP request associated with the exception.
     * @return A ResponseEntity with a status of CONFLICT and an error message.
     */
    @ExceptionHandler({ManagerHasProductsException.class,
            ManagerHasClientsException.class,
            LoginAlreadyExistsException.class,
            AgreementAlreadyExistsException.class})
    public ResponseEntity<String> conflictException(Exception exception, HttpServletRequest request) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Handle exceptions related to invalid amount inputs.
     *
     * @param exception - The exception to be handled.
     * @param request - The HTTP request associated with the exception.
     * @return A ResponseEntity with a status of BAD_REQUEST and an error message.
     */
    @ExceptionHandler
    public ResponseEntity<String> invalidAmountException(InvalidAmountException exception, HttpServletRequest request) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}