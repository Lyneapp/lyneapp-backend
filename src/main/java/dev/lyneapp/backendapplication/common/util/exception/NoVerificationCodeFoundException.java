package dev.lyneapp.backendapplication.common.util.exception;

public class NoVerificationCodeFoundException extends RuntimeException {
    public NoVerificationCodeFoundException(String message) {
        super(message);
    }
}
