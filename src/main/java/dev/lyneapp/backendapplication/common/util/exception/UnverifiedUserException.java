package dev.lyneapp.backendapplication.common.util.exception;


public class UnverifiedUserException extends RuntimeException {
    public UnverifiedUserException(String message) {
        super(message);
    }
}
