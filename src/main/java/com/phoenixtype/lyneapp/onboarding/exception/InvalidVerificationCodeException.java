package com.phoenixtype.lyneapp.onboarding.exception;

public class InvalidVerificationCodeException extends RuntimeException {
    public InvalidVerificationCodeException(String message) {
        super(message);
    }
}