package com.example.project3.service;

/**
 * An exception class used for the CarService implementation.
 */
public class CarServiceException extends RuntimeException {

    public CarServiceException(String message) {
        super(message);
    }

    public CarServiceException(String message, Throwable e) {
        super(message, e);
    }
}
