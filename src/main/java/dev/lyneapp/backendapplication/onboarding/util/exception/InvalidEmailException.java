package dev.lyneapp.backendapplication.onboarding.util.exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String message) {
        super(message);
    }
}
