package dev.lyneapp.backendapplication.settings.controller;

import dev.lyneapp.backendapplication.common.model.PhoneNumberDTO;
import dev.lyneapp.backendapplication.settings.model.DeleteAccountRequest;
import dev.lyneapp.backendapplication.settings.model.SettingsDTO;
import dev.lyneapp.backendapplication.settings.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


// TODO for each of the modules include the module name in the uri e.g. api/vi/settings/deleteAccount
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/settings")
public class SettingsController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SettingsController.class);
    private final SettingsService settingsService;

    @DeleteMapping(path = "deleteAccount")
    public ResponseEntity<String> deleteAccount(@RequestBody DeleteAccountRequest deleteAccountRequest) {
        LOGGER.info("Delete account request received for user with id: {}", deleteAccountRequest.getUserPhoneNumber());
        settingsService.deleteAccount(deleteAccountRequest);
        LOGGER.info("Account deleted successfully for user with id: {}", deleteAccountRequest.getUserPhoneNumber());
        return ResponseEntity.ok("Account deleted successfully.");
    }

    @GetMapping("all")
    public ResponseEntity<SettingsDTO> getAllSettings(PhoneNumberDTO phoneNumberRequest) {
        LOGGER.info("Get all settings request received for user with id: {}", phoneNumberRequest.getUserPhoneNumber());
        SettingsDTO settingsDTO = settingsService.getAllSettings(phoneNumberRequest);
        return ResponseEntity.ok(settingsDTO);
    }
}