package dev.lyneapp.backend.onboarding.utils.archive;

import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class PasswordValidator implements Predicate<String> {
    @Override
    public boolean test(String password) {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$");
    }
}