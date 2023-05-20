package dev.lyneapp.backendapplication.onboarding.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PhoneNumberVerificationResponse {
    private String userPhoneNumber;
    private String verificationCode;
    private boolean userIsVerified;
}