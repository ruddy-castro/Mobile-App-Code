package com.example.project3.service;

public class CarServiceException extends RuntimeException {

    public CarServiceException(String message) {
        super(message);
    }

    public CarServiceException(String message, Throwable e) {
        super(message, e);
    }
}
