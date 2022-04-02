package com.heydari.customer.exception;

public class CustomerBadRequestException extends RuntimeException{
    public CustomerBadRequestException() {
        super();
    }
    public CustomerBadRequestException(String message) {
        super(message);
    }
    public CustomerBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
