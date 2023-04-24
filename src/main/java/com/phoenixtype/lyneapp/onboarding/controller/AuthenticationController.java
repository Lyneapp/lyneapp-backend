package com.phoenixtype.lyneapp.onboarding.controller;

import com.phoenixtype.lyneapp.onboarding.model.UserLoginRequest;
import com.phoenixtype.lyneapp.onboarding.model.UserRegisterRequest;
import com.phoenixtype.lyneapp.onboarding.model.VerifyUserRequest;
import com.phoenixtype.lyneapp.onboarding.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/create_account")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        authenticationService.registerUser(request.getPhoneNumber(), request.getPassword());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/verify_phone_number")
    public ResponseEntity<?> verifyPhoneNumber(@Valid @RequestBody VerifyUserRequest request) {
        authenticationService.verifyPhoneNumber(request.getPhoneNumber(), request.getVerificationCode());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/user_login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginRequest request) {
        authenticationService.loginUser(request.getPhoneNumber(), request.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
