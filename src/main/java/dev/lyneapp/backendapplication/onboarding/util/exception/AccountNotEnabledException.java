package dev.lyneapp.backendapplication.onboarding.util.exception;

public class AccountNotEnabledException extends RuntimeException {
    public AccountNotEnabledException(String message) {
        super(message);
    }
}