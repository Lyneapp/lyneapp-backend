package dev.lyneapp.backendapplication.onboarding.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import dev.lyneapp.backendapplication.onboarding.model.ConfirmationToken;
import dev.lyneapp.backendapplication.onboarding.model.PhoneNumberLookUpData;
import dev.lyneapp.backendapplication.onboarding.model.Preference;
import dev.lyneapp.backendapplication.onboarding.model.User;
import dev.lyneapp.backendapplication.onboarding.model.request.*;
import dev.lyneapp.backendapplication.onboarding.model.response.*;
import dev.lyneapp.backendapplication.onboarding.repository.ConfirmationTokenRepository;
import dev.lyneapp.backendapplication.onboarding.repository.UserRepository;
import dev.lyneapp.backendapplication.onboarding.util.exception.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static dev.lyneapp.backendapplication.onboarding.util.VerifyUser.verifyPhoneNumberExist;
import static dev.lyneapp.backendapplication.onboarding.util.VerifyUser.verifyTokenExist;
import static dev.lyneapp.backendapplication.onboarding.util.exception.ExceptionMessages.*;

// TODO and to reply to the email or contact Lyne if someone else used their email address.

@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final static Logger LOGGER = LoggerFactory.getLogger(OnboardingService.class);
    private static final String PHONE_NUMBER_REGEX = "^\\+?[1-9]\\d{1,14}$";
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";
    private static final String CONFIRMED = "confirmed";
    private static final String CONFIRMATION_MESSAGE = "Please check your email to confirm your account";
    private static final String EMAIL_SUCCESS_MESSAGE = "Email sent, kindly check your inbox";

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Value("${twilio.account.sid}")
    private String twilioAccountSid;

    @Value("${twilio.auth.token}")
    private String twilioAuthToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    @Value("${server.host.url}")
    private String serverHostUrl;


    public void phoneNumberSignUp(PhoneNumberRequest yourPhoneNumberRequest) throws PhoneNumberAlreadyExistsException {
        boolean isValidUserPhoneNumber = yourPhoneNumberRequest.getUserPhoneNumber().matches(PHONE_NUMBER_REGEX);
        boolean existsByUserPhoneNumber = userRepository.findByUserPhoneNumber(yourPhoneNumberRequest.getUserPhoneNumber()).isPresent();

        if (!isValidUserPhoneNumber) {
            throw new InvalidPhoneNumberException(PHONE_NUMBER_IS_INVALID + yourPhoneNumberRequest.getUserPhoneNumber());
        }
        if (existsByUserPhoneNumber) {
            throw new PhoneNumberAlreadyExistsException(ExceptionMessages.PHONE_NUMBER_ALREADY_EXIST);
        }

        User user = new User();
        user.setUserPhoneNumber(yourPhoneNumberRequest.getUserPhoneNumber());
        userRepository.save(user);

        sendVerificationCode(yourPhoneNumberRequest.getUserPhoneNumber());
    }


    public PhoneNumberVerificationResponse verifyPhoneNumber(PhoneNumberVerificationRequest yourPhoneNumberVerificationRequest) throws InvalidVerificationCodeException {
        // TODO There should be a limit to how many times the user can enter a verification code (what happens when you enter the wrong code X times)
        //  TODO This should be a way to prevent users from calling the API too many times

        Optional<User> user = userRepository.findByUserPhoneNumber(yourPhoneNumberVerificationRequest.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        if (!verifiedUser.getVerificationCode().equals(yourPhoneNumberVerificationRequest.getVerificationCode())) {
            throw new InvalidVerificationCodeException(ExceptionMessages.INVALID_VERIFICATION_CODE);
        }

        com.twilio.rest.lookups.v2.PhoneNumber twilioPhoneNumber = com.twilio.rest.lookups.v2.PhoneNumber.fetcher(yourPhoneNumberVerificationRequest.getUserPhoneNumber()).fetch();
        PhoneNumberLookUpData phoneNumberLookUpData = new PhoneNumberLookUpData();

        phoneNumberLookUpData.setCallerName(twilioPhoneNumber.getCallerName());
        phoneNumberLookUpData.setCallForwarding(twilioPhoneNumber.getCallForwarding());
        phoneNumberLookUpData.setCountryCode(twilioPhoneNumber.getCountryCode());
        phoneNumberLookUpData.setIdentityMatch(twilioPhoneNumber.getIdentityMatch());
        phoneNumberLookUpData.setUrl(twilioPhoneNumber.getUrl());
        phoneNumberLookUpData.setValid(twilioPhoneNumber.getValid());
        phoneNumberLookUpData.setLiveActivity(twilioPhoneNumber.getLiveActivity());
        phoneNumberLookUpData.setSimSwap(twilioPhoneNumber.getSimSwap());
        phoneNumberLookUpData.setNationalFormat(twilioPhoneNumber.getNationalFormat());
        phoneNumberLookUpData.setCallingCountryCode(twilioPhoneNumber.getCallingCountryCode());
        phoneNumberLookUpData.setSmsPumpingRisk(twilioPhoneNumber.getSmsPumpingRisk());
        phoneNumberLookUpData.setLineTypeIntelligence(twilioPhoneNumber.getLineTypeIntelligence());
        phoneNumberLookUpData.setValidationErrors(twilioPhoneNumber.getValidationErrors());

        verifiedUser.setPhoneNumberLookUpData(phoneNumberLookUpData);
        verifiedUser.setUserIsVerified(true);
        userRepository.save(verifiedUser);

        return PhoneNumberVerificationResponse
                .builder()
                .userPhoneNumber(verifiedUser.getUserPhoneNumber())
                .verificationCode(verifiedUser.getVerificationCode())
                .userIsVerified(verifiedUser.isUserIsVerified())
                .build();
    }


    public String yourEmail(SendEmailRequest sendEmailRequest) {
        boolean isValidEmail = sendEmailRequest.getEmailAddress().matches(EMAIL_REGEX);

        if (!isValidEmail) {
            throw new InvalidPasswordException(EMAIL_IS_INVALID);
        }
        Optional<User> user = userRepository.findByUserPhoneNumber(sendEmailRequest.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);
        verifiedUser.setEmailAddress(sendEmailRequest.getEmailAddress());
        userRepository.save(verifiedUser);
        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), verifiedUser);
        confirmationTokenRepository.save(confirmationToken);
        String link = serverHostUrl + "/api/v1/auth/confirm_email_token?token=" + token;

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(sendEmailRequest.getFromEmail());
        simpleMailMessage.setTo(sendEmailRequest.getToEmail());
        simpleMailMessage.setSubject(sendEmailRequest.getSubject());
        simpleMailMessage.setText(sendEmailRequest.getBody());

        Destination destination = new Destination();
        List<String> toAddresses = new ArrayList<>();
        String[] emails = simpleMailMessage.getTo();
        Collections.addAll(toAddresses, Objects.requireNonNull(emails));
        destination.setToAddresses(toAddresses);

        SendTemplatedEmailRequest sendTemplatedEmailRequest = new SendTemplatedEmailRequest();
        sendTemplatedEmailRequest.withDestination(destination);
        sendTemplatedEmailRequest.withTemplate("EmailVerificationTemplate");
        sendTemplatedEmailRequest.withTemplateData("{ \"name\":\"" + verifiedUser.getUserPhoneNumber() + "\", \"verificationLink\": \"" + link + "\"}");
        sendTemplatedEmailRequest.withSource(simpleMailMessage.getFrom());
        amazonSimpleEmailService.sendTemplatedEmail(sendTemplatedEmailRequest);

        return CONFIRMATION_MESSAGE;
    }


    public EmailResponse confirmEmailToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ConfirmationTokenNotFoundException(CONFIRMATION_TOKEN_NOT_FOUND));

        if(confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationToken.setEmailEnabled(true);
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);

        return EmailResponse
                .builder()
                .emailAddress(confirmationToken.getUser().getEmailAddress())
                .userPhoneNumber(confirmationToken.getUser().getUserPhoneNumber())
                .token(confirmationToken.getToken())
                .emailEnabled(confirmationToken.isEmailEnabled())
                .createdAt(confirmationToken.getCreatedAt())
                .confirmedAt(confirmationToken.getConfirmedAt())
                .expiresAt(confirmationToken.getExpiresAt())
                .confirmationStatus(CONFIRMED)
                .build();
    }


    public ProfileResponse yourProfile(ProfileRequest yourProfileRequest) {
        Optional<User> user = userRepository.findByUserPhoneNumber(yourProfileRequest.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        if (!verifiedUser.isUserIsVerified()) {
            throw new UnverifiedUserException(USER_UNVERIFIED);
        }

        verifiedUser.setFirstName(yourProfileRequest.getName().getFirstName());
        verifiedUser.setLastName(yourProfileRequest.getName().getLastName());
        verifiedUser.setDateOfBirth(yourProfileRequest.getDateOfBirth());
        verifiedUser.setCurrentLocation(yourProfileRequest.getCurrentLocation());
        verifiedUser.setPlaceOfBirthLocation(yourProfileRequest.getPlaceOfBirthLocation());
        verifiedUser.setRole(yourProfileRequest.getRole());
        verifiedUser.setGender(yourProfileRequest.getGender());
        verifiedUser.setHeight(yourProfileRequest.getHeight());
        verifiedUser.setTribe(yourProfileRequest.getTribe());
        verifiedUser.setDoYouDrink(yourProfileRequest.isDoYouDrink());
        verifiedUser.setDoYouSmoke(yourProfileRequest.isDoYouSmoke());
        verifiedUser.setReligion(yourProfileRequest.getReligion());
        verifiedUser.setDoYouHaveChildren(yourProfileRequest.isDoYouHaveChildren());
        verifiedUser.setJob(yourProfileRequest.getOccupation().getJob());
        verifiedUser.setIndustry(yourProfileRequest.getOccupation().getIndustry());
        verifiedUser.setHighestDegree(yourProfileRequest.getQualification().getHighestDegree());
        verifiedUser.setInstitutionName(yourProfileRequest.getQualification().getInstitutionName());
        verifiedUser.setLanguages(yourProfileRequest.getLanguages());
        verifiedUser.setInterests(yourProfileRequest.getInterests());
        verifiedUser.setAboutYou(yourProfileRequest.getAboutYou());
        verifiedUser.setPrompts(yourProfileRequest.getPrompts());
        verifiedUser.setProfileCreated(true);

        userRepository.save(verifiedUser);

        return ProfileResponse
                .builder()
                .userPhoneNumber(verifiedUser.getUserPhoneNumber())
                .profileCreated(verifiedUser.isProfileCreated())
                .build();
    }


    public PasswordRegistrationResponse yourPassword(PasswordRequest yourPasswordRequest) {
        boolean isValidPassword = yourPasswordRequest.getPassword().matches(PASSWORD_REGEX);

        if (!isValidPassword) {
            throw new InvalidPasswordException(ExceptionMessages.PASSWORD_IS_INVALID);
        }
        if (!yourPasswordRequest.getPassword().equals(yourPasswordRequest.getConfirmPassword())) {
            throw new PasswordMismatchException(ExceptionMessages.PASSWORD_MISMATCH);
        }
        String encryptedPassword = passwordEncoder.encode(yourPasswordRequest.getPassword());

        Optional<User> user = userRepository.findByUserPhoneNumber(yourPasswordRequest.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setPasswordHash(encryptedPassword);
        verifiedUser.setPassword(encryptedPassword);
        verifiedUser.setAccountEnabled(true);
        verifiedUser.setPasswordCreatedDate(LocalDateTime.now());

        userRepository.save(verifiedUser);
        var jwtToken = jwtService.generateToken(verifiedUser);

        return PasswordRegistrationResponse.builder()
                .token(jwtToken)
                .userPhoneNumber(verifiedUser.getUserPhoneNumber())
                .passwordCreatedDate(verifiedUser.getPasswordCreatedDate())
                .accountEnabled(verifiedUser.isAccountEnabled())
                .build();
    }


    public PreferenceResponse yourPreference(PreferenceRequest preferenceRequest) {
        LOGGER.info(preferenceRequest.toString());
        Optional<User> user = userRepository.findByUserPhoneNumber(preferenceRequest.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        Preference preference = Preference.builder()
                .preferredGender(preferenceRequest.getPreferredGender())
                .preferredTribes(preferenceRequest.getPreferredTribes())
                .preferredAgeRange(preferenceRequest.getPreferredAgeRange())
                .preferredHeightRange(preferenceRequest.getPreferredHeightRange())
                .preferredReligion(preferenceRequest.getPreferredReligion())
                .doYouWantSomeoneWithChildren(preferenceRequest.getDoYouWantSomeoneWithChildren())
                .preferredQualification(preferenceRequest.getPreferredQualification())
                .preferredLocations(preferenceRequest.getPreferredLocations())
                .preferredInterests(preferenceRequest.getPreferredInterests())
                .preferredLanguages(preferenceRequest.getPreferredLanguages())
                .preferenceCreatedAt(LocalDateTime.now())
                .shouldTheyDrink(preferenceRequest.isShouldTheyDrink())
                .shouldTheySmoke(preferenceRequest.isShouldTheySmoke())
                .preferenceCreated(true)
                .build();

        verifiedUser.setPreferences(preference);
        verifiedUser.setPreferenceCreated(preference.isPreferenceCreated());
        userRepository.save(verifiedUser);

        return PreferenceResponse
                .builder()
                .preferenceCreated(preference.isPreferenceCreated())
                .userPhoneNumber(verifiedUser.getUserPhoneNumber())
                .build();
    }


    public LoginResponse loginAuthentication(LoginRequest yourLoginRequest) throws PasswordMismatchException, AccountNotEnabledException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(yourLoginRequest.getUserPhoneNumber(), yourLoginRequest.getPassword()));

        Optional<User> user = userRepository.findByUserPhoneNumber(yourLoginRequest.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        if (verifiedUser.isAccountLocked() || verifiedUser.isAccountExpired() || verifiedUser.isCredentialsExpired()) {
            throw new AccessNotAllowedException(USER_ACCESS_REVOKED);
        }
        if (!verifiedUser.isUserIsVerified()) {
            throw new UnverifiedUserException(USER_UNVERIFIED);
        }
        if (!verifiedUser.isAccountEnabled()) {
            throw new AccountNotEnabledException(ACCOUNT_NOT_ENABLED);
        }
        if (!passwordEncoder.matches(yourLoginRequest.getPassword(), verifiedUser.getPassword())) {
            throw new PasswordMismatchException(PHONE_NUMBER_IS_INVALID);
        }

        verifiedUser.setUserLoggedIn(true);
        userRepository.save(verifiedUser);

        var jwtToken = jwtService.generateToken(verifiedUser);

        return LoginResponse
                .builder()
                .token(jwtToken)
                .userLoggedIn(verifiedUser.isUserLoggedIn())
                .userPhoneNumber(verifiedUser.getUserPhoneNumber())
                .build();
    }


    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        Optional<User> user = userRepository.findByUserPhoneNumber(forgotPasswordRequest.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        String token = generateToken();
        verifiedUser.setPassword(token);
        verifiedUser.setPasswordResetToken(token);

        userRepository.save(verifiedUser);
        String link = serverHostUrl + "/api/v1/auth/reset-password?token=" + token;

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(forgotPasswordRequest.getFromEmail());
        simpleMailMessage.setTo(forgotPasswordRequest.getToEmail());
        simpleMailMessage.setSubject(forgotPasswordRequest.getSubject());
        simpleMailMessage.setText(forgotPasswordRequest.getBody());

        Destination destination = new Destination();
        List<String> toAddresses = new ArrayList<>();
        String[] emails = simpleMailMessage.getTo();
        Collections.addAll(toAddresses, Objects.requireNonNull(emails));
        destination.setToAddresses(toAddresses);

        SendTemplatedEmailRequest sendTemplatedEmailRequest = new SendTemplatedEmailRequest();
        sendTemplatedEmailRequest.withDestination(destination);
        sendTemplatedEmailRequest.withTemplate("PasswordResetTemplate");
        sendTemplatedEmailRequest.withTemplateData("{ \"name\":\"" + verifiedUser.getFirstName() + "\", \"resetLink\": \"" + link + "\"}");
        sendTemplatedEmailRequest.withSource(simpleMailMessage.getFrom());
        amazonSimpleEmailService.sendTemplatedEmail(sendTemplatedEmailRequest);
    }


    public void resetPassword(String token, ResetPasswordRequest resetPasswordRequest) {
        LOGGER.info("Token params value: " + token);
        Optional<User> user = userRepository.findByPasswordResetToken(token);
        User verifiedUser = verifyTokenExist(user);

        boolean isValidPasswordNew = resetPasswordRequest.getNewPassword().matches(PASSWORD_REGEX);
        boolean isValidPasswordConfirm = resetPasswordRequest.getConfirmNewPassword().matches(PASSWORD_REGEX);

        if (!isValidPasswordNew || !isValidPasswordConfirm) {
            throw new InvalidPasswordException(ExceptionMessages.PASSWORD_IS_INVALID);
        }

        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmNewPassword())) {
            throw new PasswordMismatchException(ExceptionMessages.PASSWORD_MISMATCH);
        }

        String encryptedPassword = passwordEncoder.encode(resetPasswordRequest.getNewPassword());
        verifiedUser.setPassword(encryptedPassword);
        verifiedUser.setPasswordUpdatedDate(LocalDateTime.now());
        verifiedUser.setPasswordResetToken(null);
        userRepository.save(verifiedUser);
    }


    private void sendVerificationCode(String phoneNumber) {

        //TODO - Error handling: Handle any exceptions that might occur during the verification code sending process.
        //TODO - Phone number validation: ensure that the phone number provided is in the correct format for the country the phone number belongs to. You can use a phone number validation library or write your own phone number validation code to ensure the phone number provided is valid.
        //TODO - Storing verification code: You might want to consider storing the verification code with an expiration time, so that if the user does not verify within a certain amount of time, the verification code is no longer valid.
        //TODO - Security: You should ensure that the verification code is only accessible by the user and not visible to other users or stored in plain text.

        Twilio.init(twilioAccountSid, twilioAuthToken);
        String verificationCode = generateVerificationCode();
        Message.creator(new com.twilio.type.PhoneNumber(phoneNumber), new com.twilio.type.PhoneNumber(twilioPhoneNumber), "Your LYNE verification code is: " + verificationCode).create();

        Optional<User> user = userRepository.findByUserPhoneNumber(phoneNumber);
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setVerificationCode(verificationCode);
        userRepository.save(verifiedUser);
    }


    private String generateVerificationCode() {
        // Generate a random 6-digit numeric verification code
        return String.format("%06d", (int) (Math.random() * 1_000_000));
    }


    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}