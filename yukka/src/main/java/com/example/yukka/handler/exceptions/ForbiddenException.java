package com.example.yukka.handler.exceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException (String message) {
        super(message);
    }
}
