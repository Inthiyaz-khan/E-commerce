package com.e_commerce.exception;

public class APIException extends RuntimeException{
    private static final long id=1l;

    public APIException(String message) {
        super(message);
    }
}
