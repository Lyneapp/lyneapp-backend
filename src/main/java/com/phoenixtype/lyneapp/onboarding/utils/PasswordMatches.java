package com.phoenixtype.lyneapp.onboarding.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static com.phoenixtype.lyneapp.onboarding.exception.message.ExceptionMessages.PASSWORD_MISMATCH;

@Documented
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatches {
    String message() default PASSWORD_MISMATCH;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
