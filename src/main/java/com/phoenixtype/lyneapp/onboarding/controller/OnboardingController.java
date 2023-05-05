package com.phoenixtype.lyneapp.onboarding.controller;

import com.phoenixtype.lyneapp.onboarding.model.MediaFile;
import com.phoenixtype.lyneapp.onboarding.model.request.UserLoginRequest;
import com.phoenixtype.lyneapp.onboarding.model.request.YourPhoneNumberRequest;
import com.phoenixtype.lyneapp.onboarding.model.request.VerifyPhoneNumberRequest;
import com.phoenixtype.lyneapp.onboarding.service.UserOnboardingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/auth/v1")
@AllArgsConstructor
public class OnboardingController {

    private static final String MEDIA_FILES_UPLOADED = "Media files uploaded successfully";
    private final UserOnboardingService userOnboardingService;

    @PostMapping(path ="phone_number_signup")
    public ResponseEntity<?> phoneNumberSignUp(@Valid @RequestBody YourPhoneNumberRequest yourPhoneNumberRequest) {
        userOnboardingService.phoneNumberSignUp(yourPhoneNumberRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(path = "verify_phone_number")
    public ResponseEntity<?> verifyPhoneNumber(@Valid @RequestBody VerifyPhoneNumberRequest verifyPhoneNumberRequest) {
        userOnboardingService.verifyPhoneNumber(verifyPhoneNumberRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping(path = "upload_media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadMedia(@RequestBody List<MediaFile> mediaFiles) {
        // Process the uploaded media files here

        return new ResponseEntity<>(MEDIA_FILES_UPLOADED, HttpStatus.OK);
    }


    @PostMapping(path = "user_login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        userOnboardingService.loginUser(userLoginRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
