package org.example.com.exception;

public class AccountExistException extends RuntimeException{
    public AccountExistException(String message) {
        super(message);
    }
}
