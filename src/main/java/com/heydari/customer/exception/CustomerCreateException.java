package com.heydari.customer.exception;

public class CustomerCreateException extends Exception{
    public CustomerCreateException() {
        super();
    }
    public CustomerCreateException(String message) {
        super(message);
    }

    public CustomerCreateException (String message, Throwable cause) {
        super(message, cause);
    }
}
