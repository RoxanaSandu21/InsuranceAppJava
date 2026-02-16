package org.example.insuranceapp.application.exception;

public class NotUniqueException extends RuntimeException {
    public NotUniqueException(String parameter) {
        super("This " + parameter + " already exists.");
    }
}
