package org.example.com.exception;

public class ManagerHasClientsException extends RuntimeException{
    public ManagerHasClientsException(String message) {
        super(message);
    }
}
