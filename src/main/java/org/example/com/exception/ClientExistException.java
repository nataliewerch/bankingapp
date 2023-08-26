package org.example.com.exception;

public class ClientExistException extends RuntimeException{
    public ClientExistException(String message) {
        super(message);
    }
}
