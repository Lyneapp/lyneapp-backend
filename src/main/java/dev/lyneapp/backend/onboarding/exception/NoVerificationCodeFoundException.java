package dev.lyneapp.backend.onboarding.exception;

public class NoVerificationCodeFoundException extends RuntimeException {
    public NoVerificationCodeFoundException(String message) {
        super(message);
    }
}
