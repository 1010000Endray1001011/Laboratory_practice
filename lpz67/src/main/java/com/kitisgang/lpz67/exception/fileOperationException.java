package com.kitisgang.lpz67.exception;

public class fileOperationException extends RuntimeException {
    public fileOperationException(String message) {
        super(message);
    }

    public fileOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}


