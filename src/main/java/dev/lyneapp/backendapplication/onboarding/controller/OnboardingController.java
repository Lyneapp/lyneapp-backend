package dev.lyneapp.backendapplication.onboarding.controller;


import dev.lyneapp.backendapplication.onboarding.model.request.*;
import dev.lyneapp.backendapplication.onboarding.model.response.*;

import dev.lyneapp.backendapplication.onboarding.service.OnboardingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO Implement logic for logout - https://youtu.be/0GGFZdYe-FY
// TODO Implement Cognito AuthZ, AuthN and phone number verification
// TODO ADD Logger as appropriate
// TODO Send the userID to the frontend to enable the user put and get their media files base don the unique ID
// TODO What happens when a user fills the onboarding form halfway - we will have to roll back and the user starts afresh. I will set a 24 hour timer that upon expiration removes all the data associated with the user from the database
// TODO Update Location data when the logs in and save to database. Login is when the user opens the app or enters credentials
// TODO implement social login with spring https://youtu.be/2WNjmT2z7c4


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/")
public class OnboardingController {

    private final static Logger LOGGER = LoggerFactory.getLogger(OnboardingController.class);
    private static final String RESET_SUCCESS_MESSAGE = "Password reset successful.";
    private static final String PASSWORD_RESET_MAIL_SUCCESS_MESSAGE = "Password reset email sent, kindly check your email";

    private final OnboardingService onboardingService;

    @PostMapping(path ="phoneNumberSignup")
    public ResponseEntity<?> phoneNumberSignUp(@Valid @RequestBody PhoneNumberRequest yourPhoneNumberRequest) {
        onboardingService.phoneNumberSignUp(yourPhoneNumberRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(path ="verifyPhoneNumber")
    public ResponseEntity<PhoneNumberVerificationResponse> verifyPhoneNumber(@Valid @RequestBody PhoneNumberVerificationRequest yourPhoneNumberVerificationRequest) {
        PhoneNumberVerificationResponse yourPhoneNumberVerificationResponse = onboardingService.verifyPhoneNumber(yourPhoneNumberVerificationRequest);
        return ResponseEntity.ok(yourPhoneNumberVerificationResponse);
    }

    @PostMapping(path = "yourEmail")
    public ResponseEntity<String> yourEmail(@Valid @RequestBody SendEmailRequest sendEmailRequest) {
        String emailSent = onboardingService.yourEmail(sendEmailRequest);
        return ResponseEntity.ok(emailSent);
    }

    @GetMapping(path = "confirmEmail")
    public ResponseEntity<EmailResponse> confirmEmailToken(@Valid @RequestParam("token") String token) {
        EmailResponse yourEmailResponse = onboardingService.confirmEmailToken(token);
        return ResponseEntity.ok(yourEmailResponse);
    }

    @PostMapping(path = "yourProfile")
    public ResponseEntity<ProfileResponse> yourProfile(@Valid @RequestBody ProfileRequest yourProfileRequest) {
        ProfileResponse yourProfileResponse = onboardingService.yourProfile(yourProfileRequest);
        return ResponseEntity.ok(yourProfileResponse);
    }

    @PostMapping(path = "yourPassword")
    public ResponseEntity<PasswordRegistrationResponse> yourPassword(@Valid @RequestBody PasswordRequest yourPasswordRequest) {
        PasswordRegistrationResponse yourPasswordRegistrationResponse = onboardingService.yourPassword(yourPasswordRequest);
        return ResponseEntity.ok(yourPasswordRegistrationResponse);
    }

    @PostMapping(path = "yourPreference")
    public ResponseEntity<PreferenceResponse> yourPreference(@Valid @RequestBody PreferenceRequest preferenceRequest) {
        PreferenceResponse preferenceResponse = onboardingService.yourPreference(preferenceRequest);
        return ResponseEntity.ok(preferenceResponse);
    }

    @PostMapping(path = "yourLogin")
    public ResponseEntity<LoginResponse> loginAuthentication(@Valid @RequestBody LoginRequest yourLoginRequest) {
        LoginResponse yourLoginResponse = onboardingService.loginAuthentication(yourLoginRequest);
        return  ResponseEntity.ok(yourLoginResponse);
    }

    @PostMapping(path = "forgotPassword")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        onboardingService.forgotPassword(forgotPasswordRequest);
        return  ResponseEntity.status(HttpStatus.OK).body(PASSWORD_RESET_MAIL_SUCCESS_MESSAGE);
    }

    @PostMapping(path = "resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody ResetPasswordRequest resetPasswordRequest) {
        onboardingService.resetPassword(token, resetPasswordRequest);
        return  ResponseEntity.status(HttpStatus.OK).body(RESET_SUCCESS_MESSAGE);
    }
}