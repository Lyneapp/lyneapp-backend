package dev.lyneapp.backendapplication.onboarding.service;

import com.amazonaws.services.cloudcontrolapi.model.InvalidCredentialsException;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import dev.lyneapp.backendapplication.common.model.UserPreference;
import dev.lyneapp.backendapplication.common.model.User;
import dev.lyneapp.backendapplication.common.repository.UserPreferenceRepository;
import dev.lyneapp.backendapplication.common.util.exception.*;
import dev.lyneapp.backendapplication.onboarding.model.*;
import dev.lyneapp.backendapplication.onboarding.model.enums.TokenType;
import dev.lyneapp.backendapplication.onboarding.model.request.*;
import dev.lyneapp.backendapplication.onboarding.model.response.*;
import dev.lyneapp.backendapplication.onboarding.repository.ConfirmationTokenRepository;
import dev.lyneapp.backendapplication.onboarding.repository.JwtTokenRepository;
import dev.lyneapp.backendapplication.common.repository.UserRepository;
import dev.lyneapp.backendapplication.onboarding.model.request.ChangePhoneNumberRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static dev.lyneapp.backendapplication.common.util.Validation.*;
import static dev.lyneapp.backendapplication.common.util.exception.ExceptionMessages.*;

// TODO and to reply to the email or contact Lyne if someone else used their email address.
// TODO reset the verification code to null when the user completes phone number verification to prevent false double verification
// TODO - Error handling: Handle any exceptions that might occur during the verification code sending process.
// TODO - Phone number validation: ensure that the phone number provided is in the correct format for the country the phone number belongs to. You can use a phone number validation library or write your own phone number validation code to ensure the phone number provided is valid.
// TODO - Storing verification code: You might want to consider storing the verification code with an expiration time, so that if the user does not verify within a certain amount of time, the verification code is no longer valid.
// TODO - Security: You should ensure that the verification code is only accessible by the user and not visible to other users or stored in plain text.
// TODO This implementation means if you don't verify the email it still saves to DB, how do we remove if verification fails
// TODO Just return a string that says email confirmed, we can return a response object if we want to include more information

@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final static Logger LOGGER = LoggerFactory.getLogger(OnboardingService.class);
    private static final int CONFIRMATION_CODE_SIZE = 8;
    private static final int HALF_CONFIRMATION_CODE_SIZE = 4;
    private static final String CONFIRMED = "confirmed";
    private static final String CONFIRMATION_MESSAGE = "Please check your email to confirm your account";
    private static final String VERIFICATION_MESSAGE = "Your LYNE verification code is: ";

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final JwtTokenRepository jwtTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final UserPreferenceRepository userPreferenceRepository;
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



    public void phoneNumberSignUp(PhoneNumberRequest phoneNumberRequest) throws PhoneNumberAlreadyExistsException, InvalidPhoneNumberException {
        LOGGER.info("Entering OnboardingService.phoneNumberSignUp");
        LOGGER.info("phoneNumberSignUp() - phoneNumberRequest: {}", phoneNumberRequest);
        validatePhoneNumber(phoneNumberRequest.getUserPhoneNumber());

        if (userRepository.findByUserPhoneNumber(phoneNumberRequest.getUserPhoneNumber()).isPresent()) {
            throw new PhoneNumberAlreadyExistsException(ExceptionMessages.PHONE_NUMBER_ALREADY_EXIST);
        }

        User user = new User();
        user.setUserPhoneNumber(phoneNumberRequest.getUserPhoneNumber());
        userRepository.save(user);

        sendVerificationCode(phoneNumberRequest.getUserPhoneNumber());
        LOGGER.info("Exiting OnboardingService.phoneNumberSignUp");
    }

    public void changePhoneNumber(ChangePhoneNumberRequest changePhoneNumberRequest) throws PhoneNumberAlreadyExistsException {
        LOGGER.info("Entering OnboardingService.changePhoneNumber: " + changePhoneNumberRequest.getUserPhoneNumber() + ", " +  changePhoneNumberRequest.getNewPhoneNumber() + ", " + changePhoneNumberRequest.getConfirmNewPhoneNumber());
        validatePhoneNumber(changePhoneNumberRequest.getUserPhoneNumber()); //regex validation
        verifyPhoneNumberIsEqual(changePhoneNumberRequest.getNewPhoneNumber(), changePhoneNumberRequest.getConfirmNewPhoneNumber()); //confirm  new phone number
        String newPhoneNumber = changePhoneNumberRequest.getNewPhoneNumber();

        Optional<User> existingUserWithNewPhoneNumber = userRepository.findByUserPhoneNumber(newPhoneNumber);
        if (existingUserWithNewPhoneNumber.isPresent()) {
            throw new PhoneNumberAlreadyExistsException(PHONE_NUMBER_ALREADY_EXIST);
        }
        User user = verifyUserByPhoneNumber(changePhoneNumberRequest.getUserPhoneNumber()); //verify user exist by phone number
        user.setUserIsVerified(false);
        user.setVerificationCode(null);
        user.setUserPhoneNumber(newPhoneNumber);
        userRepository.save(user);

        sendVerificationCode(changePhoneNumberRequest.getNewPhoneNumber());
        LOGGER.info("Exiting OnboardingService.changePhoneNumber");
    }

    public PhoneNumberVerificationResponse verifyPhoneNumber(PhoneNumberVerificationRequest phoneNumberVerificationRequest) throws InvalidVerificationCodeException {
        LOGGER.info("Entering OnboardingService.verifyPhoneNumber");
        Optional<User> user = userRepository.findByUserPhoneNumber(phoneNumberVerificationRequest.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        if (!verifiedUser.getVerificationCode().equals(phoneNumberVerificationRequest.getVerificationCode())) {
            throw new InvalidVerificationCodeException(ExceptionMessages.INVALID_VERIFICATION_CODE);
        }

        populatePhoneNumberLookUpData(verifiedUser);
        verifiedUser.setUserIsVerified(true);
        userRepository.save(verifiedUser);

        LOGGER.info("Exiting OnboardingService.verifyPhoneNumber");
        return PhoneNumberVerificationResponse.builder()
                .userPhoneNumber(verifiedUser.getUserPhoneNumber())
                .verificationCode(verifiedUser.getVerificationCode())
                .userIsVerified(verifiedUser.isUserIsVerified())
                .build();
    }

    public String yourEmail(SendEmailRequest sendEmailRequest) {
        LOGGER.info("Entering OnboardingService.yourEmail");
        validateEmail(sendEmailRequest.getEmailAddress());

        User verifiedUser = verifyUserByPhoneNumber(sendEmailRequest.getUserPhoneNumber());
        verifiedUser.setEmailAddress(sendEmailRequest.getEmailAddress());
        userRepository.save(verifiedUser);
        ConfirmationToken confirmationToken = generateConfirmationToken(verifiedUser);
        confirmationTokenRepository.save(confirmationToken);
        String confirmationCode = generateConfirmationCode(confirmationToken);

        sendConfirmEmailMessage(sendEmailRequest.getFromEmail(), sendEmailRequest.getToEmail(),
                sendEmailRequest.getSubject(), sendEmailRequest.getBody(), verifiedUser.getUserPhoneNumber(), confirmationCode);
        LOGGER.info("Exiting OnboardingService.yourEmail");
        return CONFIRMATION_MESSAGE;
    }

    public EmailResponse confirmEmailToken(ConfirmEmailRequest confirmEmailRequest) {
        LOGGER.info("Entering OnboardingService.confirmEmailToken");
        ConfirmationToken confirmationToken = findConfirmationTokenByToken(confirmEmailRequest.getConfirmationToken());
        validateConfirmationToken(confirmationToken);
        confirmationToken.setEmailEnabled(true);
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationToken.setToken(null);
        confirmationTokenRepository.save(confirmationToken);
//        confirmationTokenRepository.delete(confirmationToken);

        LOGGER.info("Exiting OnboardingService.confirmEmailToken");
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
        LOGGER.info("Entering OnboardingService.yourProfile");
        User verifiedUser = verifyUserByPhoneNumber(yourProfileRequest.getUserPhoneNumber());

        if (!verifiedUser.isUserIsVerified()) {
            throw new UnverifiedUserException(USER_UNVERIFIED);
        }

        verifiedUser.setFirstName(yourProfileRequest.getName().getFirstName());
        verifiedUser.setLastName(yourProfileRequest.getName().getLastName());
        verifiedUser.setDateOfBirth(yourProfileRequest.getDateOfBirth());
        verifiedUser.setCurrentLocation(yourProfileRequest.getCurrentLocation());
        verifiedUser.setPlaceOfBirthLocation(yourProfileRequest.getPlaceOfBirthLocation());
        verifiedUser.setRole(yourProfileRequest.getRole());
        verifiedUser.setGender(yourProfileRequest.getGender().toLowerCase());
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

        // FIXME: How do you update Age on an ongoing basis?
        // FIXME: When the user opens the app, the age should be updated
        // FIXME: What endpoint should be used to update the age?
        Period period = Period.between(yourProfileRequest.getDateOfBirth(), LocalDate.now());
        int years = period.getYears();
        verifiedUser.setAge(String.valueOf(years));

        userRepository.save(verifiedUser);
        LOGGER.info("User age is {}: ", verifiedUser.getAge());
        LOGGER.info("Exiting OnboardingService.yourProfile");
        return ProfileResponse
                .builder()
                .userPhoneNumber(verifiedUser.getUserPhoneNumber())
                .profileCreated(verifiedUser.isProfileCreated())
                .build();
    }

    public PasswordRegistrationResponse yourPassword(PasswordRequest yourPasswordRequest) {
        LOGGER.info("Entering OnboardingService.yourPassword");
        validatePassword(yourPasswordRequest);
        String encryptedPassword = passwordEncoder.encode(yourPasswordRequest.getPassword());

        User verifiedUser = verifyUserByPhoneNumber(yourPasswordRequest.getUserPhoneNumber());

        verifiedUser.setPasswordHash(encryptedPassword);
        verifiedUser.setPassword(encryptedPassword);
        verifiedUser.setAccountEnabled(true);
        verifiedUser.setPasswordCreatedDate(LocalDateTime.now());

        var savedUser = userRepository.save(verifiedUser);
        var jwtToken = jwtService.generateToken(verifiedUser);
        saveUserToken(savedUser, jwtToken);
        LOGGER.info("Exiting OnboardingService.yourPassword");
        return PasswordRegistrationResponse.builder()
                .token(jwtToken)
                .userPhoneNumber(verifiedUser.getUserPhoneNumber())
                .passwordCreatedDate(verifiedUser.getPasswordCreatedDate())
                .accountEnabled(verifiedUser.isAccountEnabled())
                .build();
    }

    public PreferenceResponse yourPreference(PreferenceRequest preferenceRequest) {
        LOGGER.info("Entering OnboardingService.yourPreference");
        User verifiedUser = verifyUserByPhoneNumber(preferenceRequest.getUserPhoneNumber());
        UserPreference preference = UserPreference.builder()
                .userPhoneNumber(preferenceRequest.getUserPhoneNumber())
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
        userPreferenceRepository.save(preference);
        userRepository.save(verifiedUser);
        LOGGER.info("Exiting OnboardingService.yourPreference");
        return PreferenceResponse
                .builder()
                .preferenceCreated(preference.isPreferenceCreated())
                .userPhoneNumber(verifiedUser.getUserPhoneNumber())
                .build();
    }

    public LoginResponse loginAuthentication(LoginRequest yourLoginRequest) throws PasswordMismatchException, AccountNotEnabledException {
        LOGGER.info("Entering OnboardingService.loginAuthentication");
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(yourLoginRequest.getUserPhoneNumber(), yourLoginRequest.getPassword()));
        } catch (AuthenticationException ex) {
            throw new InvalidCredentialsException(INVALID_CREDENTIALS_PROVIDED);
        }

        User verifiedUser = verifyUserByPhoneNumber(yourLoginRequest.getUserPhoneNumber());
        loginValidations(yourLoginRequest, verifiedUser);
        verifiedUser.setUserLoggedIn(true);
        var jwtToken = jwtService.generateToken(verifiedUser);
        revokeAllUserTokens(verifiedUser);
        saveUserToken(verifiedUser, jwtToken);
        LOGGER.info("Exiting OnboardingService.loginAuthentication");
        return LoginResponse
                .builder()
                .token(jwtToken)
                .userLoggedIn(verifiedUser.isUserLoggedIn())
                .userPhoneNumber(verifiedUser.getUserPhoneNumber())
                .build();
    }

    public String  forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        LOGGER.info("Entering OnboardingService.forgotPassword");
        User verifiedUser = verifyUserByPhoneNumber(forgotPasswordRequest.getUserPhoneNumber());

        String token = generateToken();
        verifiedUser.setPassword(token);
        verifiedUser.setPasswordResetToken(token);

        userRepository.save(verifiedUser);
        String forgotPasswordLink = generatePasswordResetLink(token);
        sendResetPasswordEmailMessage(forgotPasswordRequest, verifiedUser, forgotPasswordLink);
        LOGGER.info("Exiting OnboardingService.forgotPassword");
        return token;
    }

    public void resetPassword(String token, ResetPasswordRequest resetPasswordRequest) {
        LOGGER.info("Entering OnboardingService.resetPassword");
        User verifiedUser = verifyResetToken(token);
        validateResetPassword(resetPasswordRequest);
        String encryptedPassword = passwordEncoder.encode(resetPasswordRequest.getNewPassword());
        verifiedUser.setPassword(encryptedPassword);
        verifiedUser.setPasswordUpdatedDate(LocalDateTime.now());
        verifiedUser.setPasswordResetToken(null);
        userRepository.save(verifiedUser);
        LOGGER.info("Exiting OnboardingService.resetPassword");
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        LOGGER.info("Entering OnboardingService.changePassword");
        User verifiedUser = verifyUserByPhoneNumber(changePasswordRequest.getUserPhoneNumber());
        validateChangePassword(changePasswordRequest);
        String encryptedPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
        verifiedUser.setPassword(encryptedPassword);
        verifiedUser.setPasswordUpdatedDate(LocalDateTime.now());
        userRepository.save(verifiedUser);
        LOGGER.info("Exiting OnboardingService.changePassword");
    }

    // PRIVATE METHODS
    private void populatePhoneNumberLookUpData(User user) {
        LOGGER.info("Entering OnboardingService.populatePhoneNumberLookUpData");
        com.twilio.rest.lookups.v2.PhoneNumber twilioPhoneNumber = com.twilio.rest.lookups.v2.PhoneNumber.fetcher(user.getUserPhoneNumber()).fetch();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        PhoneNumberLookUpData phoneNumberLookUpData = modelMapper.map(twilioPhoneNumber, PhoneNumberLookUpData.class);
        user.setPhoneNumberLookUpData(phoneNumberLookUpData);
        LOGGER.info("Exiting OnboardingService.populatePhoneNumberLookUpData");
    }

    private ConfirmationToken generateConfirmationToken(User user) {
        LOGGER.info("Entering OnboardingService.generateConfirmationToken");
//        String token = UUID.randomUUID().toString();
        String token = generateRandomCode();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.plusMinutes(15);
        LOGGER.info("Exiting OnboardingService.generateConfirmationToken");
        return new ConfirmationToken(token, now, expirationTime, user);
    }

    private String generateRandomCode() {
        Random random = new Random();

        return IntStream.range(0, CONFIRMATION_CODE_SIZE)
                .mapToObj(i -> {
                    if (i < HALF_CONFIRMATION_CODE_SIZE) {
                        int letter = random.nextInt(26) + 'A'; // ASCII value of 'A' is 65
                        return String.valueOf((char) letter);
                    } else {
                        int digit = random.nextInt(10) + '0'; // ASCII value of '0' is 48
                        return String.valueOf((char) digit);
                    }
                })
                .collect(Collectors.joining());
    }

    private String generateConfirmationCode(ConfirmationToken confirmationToken) {
        LOGGER.info("Entering and exiting Onboarding.generateConfirmationCode");
        return confirmationToken.getToken();
    }

    private ConfirmationToken findConfirmationTokenByToken(String token) {
        LOGGER.info("Entering and exiting OnboardingService.findConfirmationTokenByToken");
        return confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new ConfirmationTokenNotFoundException(CONFIRMATION_TOKEN_NOT_FOUND));
    }

    private void sendVerificationCode(String phoneNumber) {
        LOGGER.info("Entering OnboardingService.sendVerificationCode");
        Twilio.init(twilioAccountSid, twilioAuthToken);
        String verificationCode = generateVerificationCode();
        Message.creator(new com.twilio.type.PhoneNumber(phoneNumber), new com.twilio.type.PhoneNumber(twilioPhoneNumber), VERIFICATION_MESSAGE + verificationCode).create();
        User verifiedUser = verifyUserByPhoneNumber(phoneNumber);

        verifiedUser.setVerificationCode(verificationCode);
        userRepository.save(verifiedUser);
        LOGGER.info("Exiting OnboardingService.sendVerificationCode");
    }

    private static Destination getDestination(SimpleMailMessage simpleMailMessage) {
        LOGGER.info("Entering OnboardingService.getDestination");
        Destination destination = new Destination();
        List<String> toAddresses = new ArrayList<>();
        String[] emails = simpleMailMessage.getTo();
        Collections.addAll(toAddresses, Objects.requireNonNull(emails));
        destination.setToAddresses(toAddresses);
        LOGGER.info("Exiting OnboardingService.getDestination");
        return destination;
    }

    private User verifyUserByPhoneNumber(String phoneNumber) {
        LOGGER.info("Entering OnboardingService.verifyUserByPhoneNumber");
        Optional<User> user = userRepository.findByUserPhoneNumber(phoneNumber);
        LOGGER.info("Exiting OnboardingService.verifyUserByPhoneNumber");
        return verifyPhoneNumberExist(user);
    }

    private String generateVerificationCode() {
        // Generate a random 6-digit numeric verification code
        LOGGER.info("Entering and exiting OnboardingService.generateVerificationCode");
        return String.format("%06d", (int) (Math.random() * 1_000_000));
    }

    private void loginValidations(LoginRequest yourLoginRequest, User verifiedUser) {
        LOGGER.info("Entering OnboardingService.loginValidations");
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
        LOGGER.info("Exiting OnboardingService.loginValidations");
    }

    private void sendConfirmEmailMessage(String fromEmail, String toEmail, String subject, String body, String userPhoneNumber, String confirmationCode) {
        LOGGER.info("Entering OnboardingService.sendConfirmEmailMessage");
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);

        Destination destination = getDestination(simpleMailMessage);

        SendTemplatedEmailRequest sendTemplatedEmailRequest = new SendTemplatedEmailRequest();
        sendTemplatedEmailRequest.withDestination(destination);
        sendTemplatedEmailRequest.withTemplate("UserEmailVerificationTemplate");
        sendTemplatedEmailRequest.withTemplateData("{ \"name\":\"" + userPhoneNumber + "\", \"verificationCode\": \"" + confirmationCode + "\"}");
        sendTemplatedEmailRequest.withSource(simpleMailMessage.getFrom());
        amazonSimpleEmailService.sendTemplatedEmail(sendTemplatedEmailRequest);
        LOGGER.info("Exiting OnboardingService.sendConfirmEmailMessage");
    }

    private String generateToken() {
        LOGGER.info("Entering and exiting OnboardingService.generateToken");
        return UUID.randomUUID().toString();
    }

    private void sendResetPasswordEmailMessage(ForgotPasswordRequest forgotPasswordRequest, User verifiedUser, String forgotPasswordLink) {
        LOGGER.info("Entering OnboardingService.sendResetPasswordEmailMessage");
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(forgotPasswordRequest.getFromEmail());
        simpleMailMessage.setTo(forgotPasswordRequest.getToEmail());
        simpleMailMessage.setSubject(forgotPasswordRequest.getSubject());
        simpleMailMessage.setText(forgotPasswordRequest.getBody());

        Destination destination = getDestination(simpleMailMessage);

        SendTemplatedEmailRequest sendTemplatedEmailRequest = new SendTemplatedEmailRequest();
        sendTemplatedEmailRequest.withDestination(destination);
        sendTemplatedEmailRequest.withTemplate("PasswordResetTemplate");
        sendTemplatedEmailRequest.withTemplateData("{ \"name\":\"" + verifiedUser.getFirstName() + "\", \"resetLink\": \"" + forgotPasswordLink + "\"}");
        sendTemplatedEmailRequest.withSource(simpleMailMessage.getFrom());
        amazonSimpleEmailService.sendTemplatedEmail(sendTemplatedEmailRequest);
        LOGGER.info("Exiting OnboardingService.sendResetPasswordEmailMessage");
    }

    private String generatePasswordResetLink(String token) {
        LOGGER.info("Entering and exiting OnboardingService.generatePasswordResetLink");
        return serverHostUrl + "/api/v1/auth/resetPassword?token=" + token;
    }

    private User verifyResetToken(String token) {
        LOGGER.info("Entering and exiting OnboardingService.verifyResetToken");
        Optional<User> user = userRepository.findByPasswordResetToken(token);
        LOGGER.info("Exiting OnboardingService.verifyResetToken");
        return verifyTokenExist(user);
    }

    private void revokeAllUserTokens(User user) {
        var validUserToken = jwtTokenRepository.findAllValidTokensByUser(user);
        LOGGER.info("Entering OnboardingService.revokeAllUserTokens");
        if (validUserToken.isEmpty()) {
            return;
        }
        validUserToken.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        jwtTokenRepository.saveAll(validUserToken);
        LOGGER.info("Exiting OnboardingService.revokeAllUserTokens");
    }

    private void saveUserToken(User user, String jwtToken) {
        LOGGER.info("Entering OnboardingService.saveUserToken");
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        jwtTokenRepository.save(token);
        LOGGER.info("Exiting OnboardingService.saveUserToken");
    }
}