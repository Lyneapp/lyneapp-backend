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
public class VerifyUserRequest {
    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 15)
    private String phoneNumber;

    @NotBlank(message = "Verification code cannot be blank.")
    @Pattern(regexp = "^\\d{6}$", message = "Verification code must have exactly 6 digits.")
    @Size(min = 6, max = 6)
    private String verificationCode;
}