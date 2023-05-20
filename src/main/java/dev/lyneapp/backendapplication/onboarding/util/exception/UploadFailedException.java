package dev.lyneapp.backendapplication.onboarding.util.exception;

import java.io.IOException;


public class UploadFailedException extends RuntimeException {
    public UploadFailedException(String message, IOException e) {
        super(message);
    }
}
