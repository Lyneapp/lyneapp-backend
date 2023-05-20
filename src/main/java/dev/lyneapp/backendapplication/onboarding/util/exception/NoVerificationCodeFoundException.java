package dev.lyneapp.backendapplication.onboarding.util.exception;

public class NoVerificationCodeFoundException extends RuntimeException {
    public NoVerificationCodeFoundException(String message) {
        super(message);
    }
}
