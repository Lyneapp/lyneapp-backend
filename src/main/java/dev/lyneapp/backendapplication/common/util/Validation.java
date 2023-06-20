package dev.lyneapp.backendapplication.common.util;

import dev.lyneapp.backendapplication.common.util.exception.*;
import dev.lyneapp.backendapplication.onboarding.model.ConfirmationToken;
import dev.lyneapp.backendapplication.common.model.User;
import dev.lyneapp.backendapplication.onboarding.model.request.ChangePasswordRequest;
import dev.lyneapp.backendapplication.onboarding.model.request.PasswordRequest;
import dev.lyneapp.backendapplication.onboarding.model.request.ResetPasswordRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static dev.lyneapp.backendapplication.common.util.exception.ExceptionMessages.*;

@Configuration
@RequiredArgsConstructor
public class Validation {

    private final static Logger LOGGER = LoggerFactory.getLogger(Validation.class);

    private static final String PHONE_NUMBER_REGEX = "^\\+?[1-9]\\d{1,14}$";
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";


    public static User verifyPhoneNumberExist(Optional<User> user) {
        LOGGER.info("Entering and exiting Validation.verifyPhoneNumberExist");
        return user.orElseThrow(() -> new PhoneNumberNotFoundException(ExceptionMessages.PHONE_NUMBER_DOES_NOT_EXIST));
    }

    public static User verifyTokenExist(Optional<User> user) {
        LOGGER.info("Entering and exiting Validation.verifyTokenExist");
        return user.orElseThrow(() -> new TokenNotFoundException(ExceptionMessages.INVALID_TOKEN));
    }

    public static void validatePhoneNumber(String phoneNumber) throws InvalidPhoneNumberException {
        LOGGER.info("Entering Validation.validatePhoneNumber");
        if (!phoneNumber.matches(PHONE_NUMBER_REGEX)) {
            LOGGER.info("Validation.validatePhoneNumber: Phone number is invalid");
            throw new InvalidPhoneNumberException(PHONE_NUMBER_IS_INVALID + phoneNumber);
        }
        LOGGER.info("Validation.validatePhoneNumber: Phone number is valid");
    }

    public static void validateEmail(String emailAddress) {
        LOGGER.info("Entering Validation.validateEmail");
        if (!emailAddress.matches(EMAIL_REGEX)) {
            LOGGER.info("Validation.validateEmail: Email is invalid");
            throw new InvalidPasswordException(EMAIL_IS_INVALID);
        }
        LOGGER.info("Validation.validateEmail: Email is valid");
    }


    public static void verifyPhoneNumberIsEqual(String userPhoneNumber1, String userPhoneNumber2) {
        LOGGER.info("Entering Validation.verifyPhoneNumberIsEqual");
        if (!Objects.equals(userPhoneNumber1, userPhoneNumber2)) {
            LOGGER.info("Validation.verifyPhoneNumberIsEqual: Phone numbers do not match");
            throw new PhoneNumberMismatchException(PHONE_NUMBER_MISMATCH);
        }
        LOGGER.info("Validation.verifyPhoneNumberIsEqual: Phone numbers match");
    }

    public static void validateConfirmationToken(ConfirmationToken confirmationToken) {
        LOGGER.info("Entering Validation.validateConfirmationToken");
        if (confirmationToken.getConfirmedAt() != null) {
            LOGGER.info("Validation.validateConfirmationToken: Email already confirmed");
            throw new EmailAlreadyExistsException(EMAIL_ALREADY_CONFIRMED);
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            LOGGER.info("Validation.validateConfirmationToken: Token expired");
            throw new TokenExpiredException(TOKEN_EXPIRED);
        }
        LOGGER.info("Validation.validateConfirmationToken: Token is valid");
    }

    public static void validatePassword(PasswordRequest yourPasswordRequest) {
        LOGGER.info("Entering Validation.validatePassword");
        String password = yourPasswordRequest.getPassword();
        String confirmPassword = yourPasswordRequest.getConfirmPassword();

        if (!password.matches(PASSWORD_REGEX)) {
            LOGGER.info("Validation.validatePassword: Password is invalid");
            throw new InvalidPasswordException(ExceptionMessages.PASSWORD_IS_INVALID);
        }

        if (!password.equals(confirmPassword)) {
            LOGGER.info("Validation.validatePassword: Passwords do not match");
            throw new PasswordMismatchException(ExceptionMessages.PASSWORD_MISMATCH);
        }
        LOGGER.info("Validation.validatePassword: Password is valid");
    }

    public static void validateResetPassword(ResetPasswordRequest resetPasswordRequest) {
        LOGGER.info("Entering Validation.validateResetPassword");
        boolean isValidPasswordNew = resetPasswordRequest.getNewPassword().matches(PASSWORD_REGEX);
        boolean isValidPasswordConfirm = resetPasswordRequest.getConfirmNewPassword().matches(PASSWORD_REGEX);

        if (!isValidPasswordNew || !isValidPasswordConfirm) {
            LOGGER.info("Validation.validateResetPassword: Password is invalid");
            throw new InvalidPasswordException(ExceptionMessages.PASSWORD_IS_INVALID);
        }

        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmNewPassword())) {
            LOGGER.info("Validation.validateResetPassword: Passwords do not match");
            throw new PasswordMismatchException(ExceptionMessages.PASSWORD_MISMATCH);
        }
        LOGGER.info("Validation.validateResetPassword: Password is valid");
    }

    public static void validateChangePassword(ChangePasswordRequest changePasswordRequest) {
        LOGGER.info("Entering Validation.validateChangePassword");
        boolean isValidPasswordExisting = changePasswordRequest.getExistingPassword().matches(PASSWORD_REGEX);
        boolean isValidPasswordNew = changePasswordRequest.getNewPassword().matches(PASSWORD_REGEX);
        boolean isValidPasswordConfirm = changePasswordRequest.getConfirmNewPassword().matches(PASSWORD_REGEX);

        if (!isValidPasswordExisting || !isValidPasswordNew || !isValidPasswordConfirm) {
            LOGGER.info("Validation.validateChangePassword: Password is invalid");
            throw new InvalidPasswordException(ExceptionMessages.PASSWORD_IS_INVALID);
        }

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) {
            LOGGER.info("Validation.validateChangePassword: Passwords do not match");
            throw new PasswordMismatchException(ExceptionMessages.PASSWORD_MISMATCH);
        }

        if (changePasswordRequest.getNewPassword().equals(changePasswordRequest.getExistingPassword())) {
            LOGGER.info("Validation.validateChangePassword: Passwords are the same");
            throw new PasswordTheSameException(ExceptionMessages.PASSWORD_THE_SAME);
        }
        LOGGER.info("Validation.validateChangePassword: Password is valid");
    }
}