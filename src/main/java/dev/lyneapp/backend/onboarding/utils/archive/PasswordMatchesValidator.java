package dev.lyneapp.backend.onboarding.utils.archive;

import dev.lyneapp.backend.onboarding.model.request.YourPasswordRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        YourPasswordRequest yourPasswordRequest = (YourPasswordRequest) obj;
        return yourPasswordRequest.getPassword().equals(yourPasswordRequest.getConfirmPassword());
    }
}
