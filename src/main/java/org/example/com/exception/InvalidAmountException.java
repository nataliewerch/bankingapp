package org.example.com.exception;

public class InvalidAmountException extends RuntimeException{

    public InvalidAmountException(String message) {
        super(message);
    }
}
