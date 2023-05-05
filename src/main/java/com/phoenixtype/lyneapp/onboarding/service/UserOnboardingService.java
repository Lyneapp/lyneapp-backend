package com.phoenixtype.lyneapp.onboarding.service;

import com.phoenixtype.lyneapp.onboarding.exception.*;
import com.phoenixtype.lyneapp.onboarding.miscellaneous.UserPrincipal;
import com.phoenixtype.lyneapp.onboarding.model.*;
import com.phoenixtype.lyneapp.onboarding.model.request.YourPasswordRequest;
import com.phoenixtype.lyneapp.onboarding.model.request.YourPhoneNumberRequest;
import com.phoenixtype.lyneapp.onboarding.model.request.UserLoginRequest;
import com.phoenixtype.lyneapp.onboarding.model.request.VerifyPhoneNumberRequest;
import com.phoenixtype.lyneapp.onboarding.repository.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

import static com.phoenixtype.lyneapp.onboarding.exception.message.ExceptionMessages.*;

@Service
@AllArgsConstructor
public class UserOnboardingService implements UserDetailsService {

    //TODO Move exception-texts to its own class


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;


    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpiration;

    @Value("${twilio.account.sid}")
    private String twilioAccountSid;

    @Value("${twilio.auth.token}")
    private String twilioAuthToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public void phoneNumberSignUp(YourPhoneNumberRequest yourPhoneNumberRequest) throws PhoneNumberAlreadyExistsException {
        if (userRepository.existsByPhoneNumber(yourPhoneNumberRequest.getPhoneNumber())) {
            throw new PhoneNumberAlreadyExistsException(PHONE_NUMBER_ALREADY_EXIST);
        }

        User user = new User();
        user.setPhoneNumber(yourPhoneNumberRequest.getPhoneNumber());
        userRepository.save(user);

        sendVerificationCode(yourPhoneNumberRequest.getPhoneNumber());
    }

    private void encodePassword(YourPasswordRequest yourPasswordRequest) {
        if (yourPasswordRequest.getPassword().equals(yourPasswordRequest.getConfirmPassword())) {
            String encryptedPassword = passwordEncoder.encode(yourPasswordRequest.getPassword());
            Optional<User> user = userRepository.findByPhoneNumber(yourPasswordRequest.getPhoneNumber());
            User verifiedUser = verifiedUserException(user);

            verifiedUser.setPassword(encryptedPassword);
            userRepository.save(verifiedUser);
        } else {
            throw new PasswordMismatchException(PASSWORD_MISMATCH);
        }
    }

    static User verifiedUserException(Optional<User> user) {
        return user.orElseThrow(() -> new PhoneNumberNotFoundException(PHONE_NUMBER_DOES_NOT_EXIST));
    }

    public void verifyPhoneNumber(VerifyPhoneNumberRequest verifyPhoneNumberRequest) throws InvalidVerificationCodeException {
        //TODO Verify the phone number and update the user's account
        //TODO There should be a limit to how many times the user can enter a verification code
        Optional<User> user = userRepository.findByPhoneVerificationCode(verifyPhoneNumberRequest.getVerificationCode());
        User verifiedUser = user.orElseThrow(() -> new NoVerificationCodeFoundException(NO_VERIFICATION_CODE_FOUND + verifyPhoneNumberRequest.getPhoneNumber()));

        if (verifiedUser.getVerificationCode().equals(verifyPhoneNumberRequest.getVerificationCode())) {
            verifiedUser.setAccountEnabled(true);
            userRepository.save(verifiedUser);
        } else {
            throw new InvalidVerificationCodeException("Invalid verification code.");
        }
    }


    public String loginUser(UserLoginRequest userLoginRequest) throws PasswordMismatchException, AccountNotEnabledException {
        // Find the user by phone number and check if the account is enabled
        //TODO password matches, then ...

        Optional<User> user = userRepository.findByPhoneNumber(userLoginRequest.getPhoneNumber());
        User verifiedUser = verifiedUserException(user);

        if (!verifiedUser.isAccountEnabled()) {
            throw new AccountNotEnabledException(ACCOUNT_NOT_ENABLED);
        }

        // Compare the provided password with the stored one
        if (!passwordEncoder.matches(userLoginRequest.getPassword(), verifiedUser.getPasswordHash())) {
            throw new PasswordMismatchException("Invalid password.");
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequest.getPhoneNumber(), userLoginRequest.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateJwtToken(userDetails);
    }

    private String generateJwtToken(UserDetails userDetails) {
        // Create the claims for the JWT token
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtExpiration); //jwtExpiration = 86400000L

        // Generate the JWT token
        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SECRET_KEY)
                .compact();
    }

    public boolean validateJwtToken(String token, UserDetails userDetails) {
        // Extract the username from the JWT token
        String username = extractUsername(token);

        // Validate the username and expiration time of the JWT token
        return (username.equals(userDetails.getUsername()) && !isJwtTokenExpired(token));
    }

    private boolean isJwtTokenExpired(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());

    }

    private String extractUsername(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    private void sendVerificationCode(String phoneNumber) {
        //TODO how do we confirm that the verification code is correct
        Twilio.init(twilioAccountSid, twilioAuthToken);

        String verificationCode = generateVerificationCode();

        Message.creator(new PhoneNumber(phoneNumber), new PhoneNumber(twilioPhoneNumber), "Your LYNE verification code is: " + verificationCode).create();

        // Store the verification code in the (DB) user's account
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        User verifiedUser = verifiedUserException(user);

        verifiedUser.setVerificationCode(verificationCode);
        userRepository.save(verifiedUser);
    }

    private String generateVerificationCode() {
        // Generate a random 6-digit numeric verification code
        return String.format("%06d", (int) (Math.random() * 1_000_000));
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        // Find the user by phone number
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        User verifiedUser = verifiedUserException(user);


        // Convert the User object to a Spring Security UserDetails object and return it
        return UserPrincipal.create(verifiedUser);
    }
}
