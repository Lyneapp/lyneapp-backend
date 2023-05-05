package com.phoenixtype.lyneapp.onboarding.utils;

import com.phoenixtype.lyneapp.onboarding.model.request.CreatePasswordRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        CreatePasswordRequest createPasswordRequest = (CreatePasswordRequest) obj;
        return createPasswordRequest.getPassword().equals(createPasswordRequest.getConfirmPassword());
    }
}
