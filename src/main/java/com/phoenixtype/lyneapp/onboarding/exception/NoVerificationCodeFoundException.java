package com.phoenixtype.lyneapp.onboarding.exception;

public class NoVerificationCodeFoundException extends RuntimeException {
    public NoVerificationCodeFoundException(String message) {
        super(message);
    }
}
