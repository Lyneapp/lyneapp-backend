package dev.lyneapp.backendapplication.onboarding.util;

import dev.lyneapp.backendapplication.onboarding.model.User;
import dev.lyneapp.backendapplication.onboarding.util.exception.PhoneNumberNotFoundException;
import dev.lyneapp.backendapplication.onboarding.util.exception.TokenNotFoundException;
import dev.lyneapp.backendapplication.onboarding.util.exception.ExceptionMessages;

import java.util.Optional;

public class VerifyUser {

    public static User verifyPhoneNumberExist(Optional<User> user) {
        return user.orElseThrow(() -> new PhoneNumberNotFoundException(ExceptionMessages.PHONE_NUMBER_DOES_NOT_EXIST));
    }

    public static User verifyTokenExist(Optional<User> user) {
        return user.orElseThrow(() -> new TokenNotFoundException(ExceptionMessages.INVALID_TOKEN));
    }
}
