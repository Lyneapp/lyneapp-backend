package dev.lyneapp.backendapplication.settings.service;

import dev.lyneapp.backendapplication.common.model.User;
import dev.lyneapp.backendapplication.common.model.PhoneNumberDTO;
import dev.lyneapp.backendapplication.onboarding.repository.ConfirmationTokenRepository;
import dev.lyneapp.backendapplication.onboarding.repository.JwtTokenRepository;
import dev.lyneapp.backendapplication.common.repository.UserRepository;
import dev.lyneapp.backendapplication.settings.model.DeleteAccountRequest;
import dev.lyneapp.backendapplication.settings.model.SettingsDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static dev.lyneapp.backendapplication.common.util.Validation.*;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AdjustPreferencesService.class);

    private final UserRepository userRepository;
    private final JwtTokenRepository jwtTokenRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;


    public void deleteAccount(DeleteAccountRequest deleteAccountRequest) {
        LOGGER.info("Entering SettingsService.deleteAccount");
        validatePhoneNumber(deleteAccountRequest.getUserPhoneNumber());
        User user = verifyUserByPhoneNumber(deleteAccountRequest.getUserPhoneNumber());
        userRepository.delete(user);
        jwtTokenRepository.deleteAllByUser(user);
        confirmationTokenRepository.deleteAllByUser(user);
        LOGGER.info("Exiting SettingsService.deleteAccount");
    }

    public User verifyUserByPhoneNumber(String phoneNumber) {
        LOGGER.info("Entering SettingsService.verifyUserByPhoneNumber");
        Optional<User> user = userRepository.findByUserPhoneNumber(phoneNumber);
        LOGGER.info("Exiting SettingsService.verifyUserByPhoneNumber");
        return verifyPhoneNumberExist(user);
    }

    public SettingsDTO getAllSettings(PhoneNumberDTO phoneNumberRequest) {
        LOGGER.info("Entering SettingsService.getAllSettings");
        User user = verifyUserByPhoneNumber(phoneNumberRequest.getUserPhoneNumber());

//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//        SettingsDTO settingsDTO = modelMapper.map(user, SettingsDTO.class);

        SettingsDTO settingsDTO = SettingsDTO.builder()
                .userPhoneNumber(user.getUserPhoneNumber())
                .emailAddress(user.getEmailAddress())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .blockedUsersCount(String.valueOf(user.getBlockedUsers().size()))
                .blockedUsers(user.getBlockedUsers())
                .blockedUsers(user.getBlockedUsers())
                .visibilityIcon(user.getSettings().isVisibilityIcon())
                .showMeOnLyne(user.getSettings().isShowMeOnLyne())
                .appleConnected(user.getSettings().isAppleConnected())
                .instagramConnected(user.getSettings().isInstagramConnected())
                .facebookConnected(user.getSettings().isFacebookConnected())
                .newMatchNotification(user.getSettings().isNewMatchNotification())
                .newMessageNotification(user.getSettings().isNewMessageNotification())
                .newMessageLikeNotification(user.getSettings().isNewMessageLikeNotification())
                .inAppVibration(user.getSettings().isInAppVibration())
                .bouquetReceived(user.getSettings().isBouquetReceived())
                .inAppSound(user.getSettings().isInAppSound())
                .build();
        LOGGER.info("Exiting SettingsService.getAllSettings");
        return settingsDTO;
    }
}