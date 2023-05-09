package dev.lyneapp.backend.onboarding.controller;


import dev.lyneapp.backend.onboarding.service.UserOnboardingService;
import dev.lyneapp.backend.onboarding.model.request.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static dev.lyneapp.backend.onboarding.exception.message.ExceptionMessages.MEDIA_FILES_UPLOADED;


@RestController
@RequestMapping("/api/auth/v1")
@AllArgsConstructor
public class OnboardingController {


    private final UserOnboardingService userOnboardingService;

    @PostMapping(path ="phone_number_signup")
    public ResponseEntity<?> phoneNumberSignUp(@Valid @RequestBody YourPhoneNumberRequest yourPhoneNumberRequest) {
        userOnboardingService.phoneNumberSignUp(yourPhoneNumberRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(path ="verify_phone_number")
    public ResponseEntity<?> verifyPhoneNumber(@Valid @RequestBody VerifyPhoneNumberRequest verifyPhoneNumberRequest) {
        userOnboardingService.verifyPhoneNumber(verifyPhoneNumberRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(path = "your_media_content", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> yourMediaContent(@Valid @RequestBody YourMediaContentRequest yourMediaContentRequest) {
        userOnboardingService.yourMediaContent(yourMediaContentRequest);
        return new ResponseEntity<>(MEDIA_FILES_UPLOADED, HttpStatus.OK);
    }


    @PostMapping(path = "your_password")
    public ResponseEntity<?> yourPassword(@Valid @RequestBody YourPasswordRequest yourPasswordRequest) {
        userOnboardingService.yourPassword(yourPasswordRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "your_profile")
    public ResponseEntity<?> yourProfile(@Valid @RequestBody YourProfileRequest yourProfileRequest) {
        userOnboardingService.yourProfile(yourProfileRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "user_login")
    public ResponseEntity<?> loginUser(@jakarta.validation.Valid @RequestBody UserLoginRequest userLoginRequest) {
        userOnboardingService.loginUser(userLoginRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    //TODO - Reset password
    //TODO Preferences Flow
}
