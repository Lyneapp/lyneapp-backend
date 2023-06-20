package dev.lyneapp.backendapplication.settings.service;

import dev.lyneapp.backendapplication.common.model.User;
import dev.lyneapp.backendapplication.onboarding.repository.ConfirmationTokenRepository;
import dev.lyneapp.backendapplication.onboarding.repository.JwtTokenRepository;
import dev.lyneapp.backendapplication.common.repository.UserRepository;
import dev.lyneapp.backendapplication.settings.model.DeleteAccountRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static dev.lyneapp.backendapplication.common.util.Validation.*;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SettingsService.class);

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

    private User verifyUserByPhoneNumber(String phoneNumber) {
        LOGGER.info("Entering SettingsService.verifyUserByPhoneNumber");
        Optional<User> user = userRepository.findByUserPhoneNumber(phoneNumber);
        LOGGER.info("Exiting SettingsService.verifyUserByPhoneNumber");
        return verifyPhoneNumberExist(user);
    }
}