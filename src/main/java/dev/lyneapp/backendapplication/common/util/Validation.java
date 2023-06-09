package dev.lyneapp.backendapplication.common.util;

import dev.lyneapp.backendapplication.common.util.exception.*;
import dev.lyneapp.backendapplication.onboarding.model.ConfirmationToken;
import dev.lyneapp.backendapplication.common.model.User;
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
        return user.orElseThrow(() -> new PhoneNumberNotFoundException(ExceptionMessages.PHONE_NUMBER_DOES_NOT_EXIST));
    }

    public static User verifyTokenExist(Optional<User> user) {
        return user.orElseThrow(() -> new TokenNotFoundException(ExceptionMessages.INVALID_TOKEN));
    }

    public static void validatePhoneNumber(String phoneNumber) throws InvalidPhoneNumberException {
        if (!phoneNumber.matches(PHONE_NUMBER_REGEX)) {
            throw new InvalidPhoneNumberException(PHONE_NUMBER_IS_INVALID + phoneNumber);
        }
    }

    public static void validateEmail(String emailAddress) {
        if (!emailAddress.matches(EMAIL_REGEX)) {
            throw new InvalidPasswordException(EMAIL_IS_INVALID);
        }
    }


    public static void verifyPhoneNumberIsEqual(String userPhoneNumber1, String userPhoneNumber2) {
        if (!Objects.equals(userPhoneNumber1, userPhoneNumber2)) {
            throw new PhoneNumberMismatchException(PHONE_NUMBER_MISMATCH);
        }
    }

    public static void validateConfirmationToken(ConfirmationToken confirmationToken) {
        if (confirmationToken.getConfirmedAt() != null) {
            throw new EmailAlreadyExistsException(EMAIL_ALREADY_CONFIRMED);
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException(TOKEN_EXPIRED);
        }
    }

    public static void validatePassword(PasswordRequest yourPasswordRequest) {
        String password = yourPasswordRequest.getPassword();
        String confirmPassword = yourPasswordRequest.getConfirmPassword();

        if (!password.matches(PASSWORD_REGEX)) {
            throw new InvalidPasswordException(ExceptionMessages.PASSWORD_IS_INVALID);
        }

        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException(ExceptionMessages.PASSWORD_MISMATCH);
        }
    }

    public static void validateResetPassword(ResetPasswordRequest resetPasswordRequest) {
        boolean isValidPasswordNew = resetPasswordRequest.getNewPassword().matches(PASSWORD_REGEX);
        boolean isValidPasswordConfirm = resetPasswordRequest.getConfirmNewPassword().matches(PASSWORD_REGEX);

        if (!isValidPasswordNew || !isValidPasswordConfirm) {
            throw new InvalidPasswordException(ExceptionMessages.PASSWORD_IS_INVALID);
        }

        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmNewPassword())) {
            throw new PasswordMismatchException(ExceptionMessages.PASSWORD_MISMATCH);
        }
    }
}
