package dev.lyneapp.backend.onboarding.service;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import dev.lyneapp.backend.onboarding.exception.*;
import dev.lyneapp.backend.onboarding.exception.message.ExceptionMessages;
import dev.lyneapp.backend.onboarding.model.User;
import dev.lyneapp.backend.onboarding.model.request.*;
import dev.lyneapp.backend.onboarding.repository.UserRepository;
import dev.lyneapp.backend.onboarding.utils.archive.PasswordValidator;
import dev.lyneapp.backend.onboarding.utils.archive.PhoneNumberValidator;
import dev.lyneapp.backend.onboarding.utils.UserPrincipal;
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
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static dev.lyneapp.backend.onboarding.exception.message.ExceptionMessages.ACCOUNT_NOT_ENABLED;
import static dev.lyneapp.backend.onboarding.exception.message.ExceptionMessages.INVALID_VERIFICATION_CODE;


@Service
@AllArgsConstructor
public class UserOnboardingService implements UserDetailsService {

    //TODO Move exception-texts to its own class


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhoneNumberValidator phoneNumberValidator;
    private final PasswordValidator passwordValidator;
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

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        // Find the user by phone number
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        User verifiedUser = verifyPhoneNumberExist(user);


        // Convert the User object to a Spring Security UserDetails object and return it
        return UserPrincipal.create(verifiedUser);
    }

    public void phoneNumberSignUp(YourPhoneNumberRequest yourPhoneNumberRequest) throws PhoneNumberAlreadyExistsException {
        boolean isValidPhoneNumber = phoneNumberValidator.test(yourPhoneNumberRequest.getPhoneNumber());

        if (!isValidPhoneNumber) {
            throw new InvalidPhoneNumberException(ExceptionMessages.PHONE_NUMBER_IS_INVALID + yourPhoneNumberRequest.getPhoneNumber());
        }

        if (userRepository.existsByPhoneNumber(yourPhoneNumberRequest.getPhoneNumber())) {
            throw new PhoneNumberAlreadyExistsException(ExceptionMessages.PHONE_NUMBER_ALREADY_EXIST);
        }

        User user = new User();
        user.setPhoneNumber(yourPhoneNumberRequest.getPhoneNumber());
        userRepository.save(user);

        sendVerificationCode(yourPhoneNumberRequest.getPhoneNumber());
    }


    private void sendVerificationCode(String phoneNumber) {
        //TODO how do we confirm that the verification code is correct
        Twilio.init(twilioAccountSid, twilioAuthToken);

        String verificationCode = generateVerificationCode();

        Message.creator(new PhoneNumber(phoneNumber), new PhoneNumber(twilioPhoneNumber), "Your LYNE verification code is: " + verificationCode).create();

        // Store the verification code in the (DB) user's account
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setVerificationCode(verificationCode);
        userRepository.save(verifiedUser);
    }

    private String generateVerificationCode() {
        // Generate a random 6-digit numeric verification code
        return String.format("%06d", (int) (Math.random() * 1_000_000));
    }

    static User verifyPhoneNumberExist(Optional<User> user) {
        return user.orElseThrow(() -> new PhoneNumberNotFoundException(ExceptionMessages.PHONE_NUMBER_DOES_NOT_EXIST));
    }

    public void verifyPhoneNumber(VerifyPhoneNumberRequest verifyPhoneNumberRequest) throws InvalidVerificationCodeException {
        //TODO Verify the phone number and update the user's account
        //TODO There should be a limit to how many times the user can enter a verification code
        Optional<User> user = userRepository.findByPhoneNumberVerificationCode(verifyPhoneNumberRequest.getVerificationCode());
        User verifiedUser = user.orElseThrow(() -> new NoVerificationCodeFoundException(ExceptionMessages.NO_VERIFICATION_CODE_FOUND + verifyPhoneNumberRequest.getPhoneNumber()));

        if (verifiedUser.getVerificationCode().equals(verifyPhoneNumberRequest.getVerificationCode())) {
            verifiedUser.setUserIsVerified(true);
            userRepository.save(verifiedUser);
        } else {
            throw new InvalidVerificationCodeException(INVALID_VERIFICATION_CODE);
        }
    }

    public void yourMediaContent(YourMediaContentRequest yourMediaContentRequest) {
        Optional<User> user = userRepository.findByPhoneNumber(yourMediaContentRequest.getPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setMediaFiles(yourMediaContentRequest.getMediaFiles());
        userRepository.save(verifiedUser);
    }


    public void yourPassword(YourPasswordRequest yourPasswordRequest) {
        boolean isValidPassword = passwordValidator.test(yourPasswordRequest.getPassword());

        if (!isValidPassword) {
            throw new InvalidPasswordException(ExceptionMessages.PASSWORD_IS_INVALID);
        }

        if (yourPasswordRequest.getPassword().equals(yourPasswordRequest.getConfirmPassword())) {
            String encryptedPassword = passwordEncoder.encode(yourPasswordRequest.getPassword());
            Optional<User> user = userRepository.findByPhoneNumber(yourPasswordRequest.getPhoneNumber());
            User verifiedUser = verifyPhoneNumberExist(user);

            verifiedUser.setPassword(encryptedPassword);

            verifiedUser.setAccountEnabled(true);
            verifiedUser.setCreatedDate(LocalDateTime.now());
            userRepository.save(verifiedUser);
        } else {
            throw new PasswordMismatchException(ExceptionMessages.PASSWORD_MISMATCH);
        }
    }

    public void yourProfile(YourProfileRequest yourProfileRequest) {
        Optional<User> user = userRepository.findByPhoneNumber(yourProfileRequest.getYourPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setFirstName(yourProfileRequest.getYourName().getFirstName());
        verifiedUser.setLastName(yourProfileRequest.getYourName().getLastName());
        verifiedUser.setEmailAddress(yourProfileRequest.getYourEmailAddress());
        verifiedUser.setDateOfBirth(yourProfileRequest.getYourDateOfBirth());
        verifiedUser.setCurrentLocation(yourProfileRequest.getYourCurrentLocation());
        verifiedUser.setPlaceOfBirthLocation(yourProfileRequest.getYourPlaceOfBirthLocation());
        verifiedUser.setGender(yourProfileRequest.getYourGender());
        verifiedUser.setHeight(yourProfileRequest.getYourHeight());
        verifiedUser.setTribe(yourProfileRequest.getYourTribe());
        verifiedUser.setDoYouDrink(yourProfileRequest.isDoYouDrink());
        verifiedUser.setDoYouSmoke(yourProfileRequest.isDoYouSmoke());
        verifiedUser.setReligion(yourProfileRequest.getYourReligion());
        verifiedUser.setDoYouHaveChildren(yourProfileRequest.isDoYouHaveChildren());
        verifiedUser.setJob(yourProfileRequest.getYourOccupation().getJob());
        verifiedUser.setIndustry(yourProfileRequest.getYourOccupation().getIndustry());
        verifiedUser.setIndustry(yourProfileRequest.getYourOccupation().getIndustry());
        verifiedUser.setHighestDegree(yourProfileRequest.getYourQualification().getHighestDegree());
        verifiedUser.setInstitutionName(yourProfileRequest.getYourQualification().getInstitutionName());
        verifiedUser.setLanguages(yourProfileRequest.getYourLanguages());
        verifiedUser.setInterests(yourProfileRequest.getYourInterests());
        verifiedUser.setAboutYou(yourProfileRequest.getAboutYou());
        verifiedUser.setPrompts(yourProfileRequest.getPrompts());

        userRepository.save(verifiedUser);
    }

    public String loginUser(UserLoginRequest userLoginRequest) throws PasswordMismatchException, AccountNotEnabledException {
        // Find the user by phone number and check if the account is enabled
        //TODO password matches, then ...

        Optional<User> user = userRepository.findByPhoneNumber(userLoginRequest.getPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

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

}
