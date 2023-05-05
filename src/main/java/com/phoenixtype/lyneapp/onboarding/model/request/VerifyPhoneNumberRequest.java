package com.phoenixtype.lyneapp.onboarding.model.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Builder
@Validated
public class VerifyPhoneNumberRequest {
    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Size(max = 15)
    private String phoneNumber;

    @NotBlank(message = "Verification code cannot be blank.")
    @Pattern(regexp = "^\\d{6}$", message = "Verification code must have exactly 6 digits.")
    @Size(min = 6, max = 6)
    private String verificationCode;

    private boolean isVerified;
}