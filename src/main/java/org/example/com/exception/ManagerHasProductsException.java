package org.example.com.exception;

public class ManagerHasProductsException extends RuntimeException{
    public ManagerHasProductsException(String message) {
        super(message);
    }
}
