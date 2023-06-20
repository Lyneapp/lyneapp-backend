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
// TODO Implement Cognito AuthZ, AuthN
// TODO ADD Logger as appropriate
// TODO What happens when a user fills the onboarding form halfway - we will have to roll back and the user starts afresh. I will set a 24 hour timer that upon expiration removes all the data associated with the user from the database
// TODO Update Location data when the logs in and save to database. Login is when the user opens the app or enters credentials
// TODO implement social login with spring https://youtu.be/2WNjmT2z7c4

// TODO for each of the modules include the module name in the uri e.g. api/vi/onboarding/phoneNumberSignup

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
        LOGGER.info("Phone number signup request received for user with phone number: {}", yourPhoneNumberRequest.getUserPhoneNumber());
        onboardingService.phoneNumberSignUp(yourPhoneNumberRequest);
        LOGGER.info("Phone number signup request completed successfully for user with phone number: {}", yourPhoneNumberRequest.getUserPhoneNumber());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(path = "changePhoneNumber")
    public ResponseEntity<String> changePhoneNumber(@RequestBody ChangePhoneNumberRequest changePhoneNumberRequest) {
        LOGGER.info("Change phone number request received for user with phone number: {}", changePhoneNumberRequest.getUserPhoneNumber());
        onboardingService.changePhoneNumber(changePhoneNumberRequest);
        LOGGER.info("Change phone number request completed successfully for user with phone number: {}", changePhoneNumberRequest.getUserPhoneNumber());
        return ResponseEntity.ok("Phone number changed successfully.");
    }

    @PostMapping(path ="verifyPhoneNumber")
    public ResponseEntity<PhoneNumberVerificationResponse> verifyPhoneNumber(@Valid @RequestBody PhoneNumberVerificationRequest yourPhoneNumberVerificationRequest) {
        LOGGER.info("Phone number verification request received for user with phone number: {}", yourPhoneNumberVerificationRequest.getUserPhoneNumber());
        PhoneNumberVerificationResponse yourPhoneNumberVerificationResponse = onboardingService.verifyPhoneNumber(yourPhoneNumberVerificationRequest);
        LOGGER.info("Phone number verification request completed successfully for user with phone number: {}", yourPhoneNumberVerificationRequest.getUserPhoneNumber());
        return ResponseEntity.ok(yourPhoneNumberVerificationResponse);
    }

    @PostMapping(path = "yourEmail")
    public ResponseEntity<String> yourEmail(@Valid @RequestBody SendEmailRequest sendEmailRequest) {
        LOGGER.info("Email sent request received for user with email: {}", sendEmailRequest.getToEmail());
        String emailSent = onboardingService.yourEmail(sendEmailRequest);
        LOGGER.info("Email sent request completed successfully for user with email: {}", sendEmailRequest.getToEmail());
        return ResponseEntity.ok(emailSent);
    }

    @GetMapping(path = "confirmEmail")
    public ResponseEntity<EmailResponse> confirmEmailToken(@Valid @RequestParam("token") String token) {
        LOGGER.info("Email confirmation request received for user with token: {}", token);
        EmailResponse yourEmailResponse = onboardingService.confirmEmailToken(token);
        LOGGER.info("Email confirmation request completed successfully for user with token: {}", token);
        return ResponseEntity.ok(yourEmailResponse);
    }

    @PostMapping(path = "yourProfile")
    public ResponseEntity<ProfileResponse> yourProfile(@Valid @RequestBody ProfileRequest yourProfileRequest) {
        LOGGER.info("Profile request received for user with phone number: {}", yourProfileRequest.getUserPhoneNumber());
        ProfileResponse yourProfileResponse = onboardingService.yourProfile(yourProfileRequest);
        LOGGER.info("Profile request completed successfully for user with phone number: {}", yourProfileRequest.getUserPhoneNumber());
        return ResponseEntity.ok(yourProfileResponse);
    }

    @PostMapping(path = "yourPassword")
    public ResponseEntity<PasswordRegistrationResponse> yourPassword(@Valid @RequestBody PasswordRequest yourPasswordRequest) {
        LOGGER.info("Password registration request received for user with phone number: {}", yourPasswordRequest.getUserPhoneNumber());
        PasswordRegistrationResponse yourPasswordRegistrationResponse = onboardingService.yourPassword(yourPasswordRequest);
        LOGGER.info("Password registration request completed successfully for user with phone number: {}", yourPasswordRequest.getUserPhoneNumber());
        return ResponseEntity.ok(yourPasswordRegistrationResponse);
    }

    @PostMapping(path = "yourPreference")
    public ResponseEntity<PreferenceResponse> yourPreference(@Valid @RequestBody PreferenceRequest preferenceRequest) {
        LOGGER.info("Preference request received for user with phone number: {}", preferenceRequest.getUserPhoneNumber());
        PreferenceResponse preferenceResponse = onboardingService.yourPreference(preferenceRequest);
        LOGGER.info("Preference request completed successfully for user with phone number: {}", preferenceRequest.getUserPhoneNumber());
        return ResponseEntity.ok(preferenceResponse);
    }

    @PostMapping(path = "yourLogin")
    public ResponseEntity<LoginResponse> loginAuthentication(@Valid @RequestBody LoginRequest yourLoginRequest) {
        LOGGER.info("Login request received for user with phone number: {}", yourLoginRequest.getUserPhoneNumber());
        LoginResponse yourLoginResponse = onboardingService.loginAuthentication(yourLoginRequest);
        LOGGER.info("Login request completed successfully for user with phone number: {}", yourLoginRequest.getUserPhoneNumber());
        return  ResponseEntity.ok(yourLoginResponse);
    }

    @PostMapping(path = "forgotPassword")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        LOGGER.info("Forgot password request received for user with phone number: {}", forgotPasswordRequest.getUserPhoneNumber());
        String resetPasswordToken = onboardingService.forgotPassword(forgotPasswordRequest);
        LOGGER.info("Forgot password request completed successfully for user with phone number: {}", forgotPasswordRequest.getUserPhoneNumber());
        return  ResponseEntity.status(HttpStatus.OK).body(PASSWORD_RESET_MAIL_SUCCESS_MESSAGE + " your temporary password is: " + resetPasswordToken);
    }

    @PostMapping(path = "resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody ResetPasswordRequest resetPasswordRequest) {
        LOGGER.info("Reset password request received for user with token: {}", token);
        onboardingService.resetPassword(token, resetPasswordRequest);
        LOGGER.info("Reset password request completed successfully for user with token: {}", token);
        return  ResponseEntity.status(HttpStatus.OK).body(RESET_SUCCESS_MESSAGE);
    }

    @PostMapping(path = "changePassword")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        LOGGER.info("Change password request received for user with phone number: {}", changePasswordRequest.getUserPhoneNumber());
        onboardingService.changePassword(changePasswordRequest);
        LOGGER.info("Change password request completed successfully for user with phone number: {}", changePasswordRequest.getUserPhoneNumber());
        return  ResponseEntity.status(HttpStatus.OK).body("Password changed successfully.");
    }
}