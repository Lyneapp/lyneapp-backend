package com.phoenixtype.lyneapp.onboarding.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginRequest {
    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 15)
    private String phoneNumber;

/**
    At least 8 characters long,
    Contains at least one uppercase letter,
    Contains at least one lowercase letter,
    Contains at least one number,
    Contains at least one special character,
    Does not contain whitespace.
 */

    @NotBlank(message = "password cannot be blank.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$", message = "Password must have at least 8 and at most of 20 characters and contain at least one uppercase letter, one lowercase letter, one number, and one special character.")
    @Size(min = 8, max = 20, message = "Password cannot be longer than 20 nor shorter than 8 characters.")
    private String password;
}