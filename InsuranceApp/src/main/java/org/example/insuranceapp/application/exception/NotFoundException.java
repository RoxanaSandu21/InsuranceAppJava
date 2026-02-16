package org.example.insuranceapp.application.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String parameter) {
        super(parameter + " was not found.");
    }
}
