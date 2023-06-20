package dev.lyneapp.backendapplication.common.util.exception;

public class PasswordTheSameException extends RuntimeException {
    public PasswordTheSameException(String message) {
        super(message);
    }
}