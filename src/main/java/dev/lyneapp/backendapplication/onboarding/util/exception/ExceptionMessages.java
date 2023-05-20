package dev.lyneapp.backendapplication.onboarding.util.exception;

public class ExceptionMessages {
    public static final String PASSWORD_MISMATCH = "The passwords entered do not match.";
    public static final String PHONE_NUMBER_DOES_NOT_EXIST = "This Phone number was not found.";
    public static final String ACCOUNT_NOT_ENABLED = "Thia account has not been enabled.";
    public static final String PHONE_NUMBER_ALREADY_EXIST = "Phone number already registered.";
    public static final String PHONE_NUMBER_IS_INVALID = "This phone number invalid.";
    public static final String PASSWORD_IS_INVALID = "This password is invalid.";
    public static final String EMAIL_IS_INVALID = "This email invalid.";
    public static final String INVALID_VERIFICATION_CODE = "Invalid verification code.";
    public static final String USER_UNVERIFIED = "This user is unverified";
    public static final String CONFIRMATION_TOKEN_NOT_FOUND = "Confirmation token not found";
    public static final String USER_NOT_FOUND_WITH_ID = "User with this ID was not found: ";
    public static final Object FAILED_TO_UPLOAD_IMAGE_S3 = "Failed to upload image to S3";
    public static final String USER_ID_DOES_NOT_EXIST = "This user ID does not exist";
    //TODO Make this non-generic
    public static final String USER_ACCESS_REVOKED = "User access has been revoked";
    public static final String INVALID_TOKEN = "This email token is invalid or expired";
    public final static String NO_VERIFICATION_CODE_FOUND = "No verification code was found for the user with this phone number: ";
    public static final String MEDIA_FILES_UPLOADED = "Media files uploaded successfully.";
    public static final String CONFIRMATION_CODE_DOES_NOT_EXIST = "This confirmation code does not exist for this email";
}
