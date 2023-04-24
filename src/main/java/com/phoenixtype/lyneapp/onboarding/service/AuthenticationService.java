package com.phoenixtype.lyneapp.onboarding.service;

import com.phoenixtype.lyneapp.onboarding.exception.AccountNotEnabledException;
import com.phoenixtype.lyneapp.onboarding.exception.InvalidVerificationCodeException;
import com.phoenixtype.lyneapp.onboarding.exception.PasswordMismatchException;
import com.phoenixtype.lyneapp.onboarding.exception.PhoneNumberAlreadyExistsException;
import com.phoenixtype.lyneapp.onboarding.miscellaneous.UserPrincipal;
import com.phoenixtype.lyneapp.onboarding.model.User;
import com.phoenixtype.lyneapp.onboarding.repository.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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

@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;

    public AuthenticationService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

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

    public void registerUser(String phoneNumber, String password) throws PhoneNumberAlreadyExistsException {
        // Check if the user already exists and throw an exception if necessary
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new PhoneNumberAlreadyExistsException("Phone number already registered.");
        }

        // Save the new user to the database
        String encryptedPassword = passwordEncoder.encode(password);
        User newUser = new User();
        newUser.setPhoneNumber(phoneNumber);
        newUser.setPassword(encryptedPassword);
        userRepository.save(newUser);

        // Send the verification code via SMS using the Twilio API
        sendVerificationCode(phoneNumber);
    }

    public void verifyPhoneNumber(String phoneNumber, String verificationCode) throws InvalidVerificationCodeException {
        // Verify the phone number and update the user's account
        //TODO There should be a limit to how many times the user can enter a verification code
        User user = userRepository.findByPhoneNumber(phoneNumber);

        if (user.getVerificationCode().equals(verificationCode)) {
            user.setAccountEnabled(true);
            userRepository.save(user);
        } else {
            throw new InvalidVerificationCodeException("Invalid verification code.");
        }
    }

    public String loginUser(String phoneNumber, String password) throws PasswordMismatchException, AccountNotEnabledException {
        // Find the user by phone number and check if the account is enabled
        //TODO password matches, then ...

        User user = userRepository.findByPhoneNumber(phoneNumber);

        if (!user.isAccountEnabled()) {
            throw new AccountNotEnabledException("Account not enabled. Please verify your phone number.");
        }

        // Compare the provided password with the stored one
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new PasswordMismatchException("Invalid password.");
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phoneNumber, password));

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

        Message.creator(new PhoneNumber(phoneNumber),
                        new PhoneNumber(twilioPhoneNumber),
                        "Your LYNE verification code is: " + verificationCode)
                .create();

        // Store the verification code in the user's account
        User user = userRepository.findByPhoneNumber(phoneNumber);
        user.setVerificationCode(verificationCode);
        userRepository.save(user);
    }

    private String generateVerificationCode() {
        // Generate a random 6-digit numeric verification code
        return String.format("%06d", (int) (Math.random() * 1_000_000));
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        // Find the user by phone number
        User user = userRepository.findByPhoneNumber(phoneNumber);

        // If the user is not found, throw a UsernameNotFoundException
        if (user == null) {
            throw new UsernameNotFoundException("User not found with phone number: " + phoneNumber);
        }

        // Convert the User object to a Spring Security UserDetails object and return it
        return UserPrincipal.create(user);
    }
}
