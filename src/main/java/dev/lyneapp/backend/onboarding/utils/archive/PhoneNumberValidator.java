package dev.lyneapp.backend.onboarding.utils.archive;

import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class PhoneNumberValidator implements Predicate<String> {
    @Override
    public boolean test(String phoneNumber) {
        return phoneNumber.matches("^\\+?[1-9]\\d{1,14}$");
    }
}




