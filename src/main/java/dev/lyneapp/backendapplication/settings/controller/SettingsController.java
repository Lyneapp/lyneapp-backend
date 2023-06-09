package dev.lyneapp.backendapplication.settings.controller;

import dev.lyneapp.backendapplication.settings.model.DeleteAccountRequest;
import dev.lyneapp.backendapplication.settings.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


// TODO for each of the modules include the module name in the uri e.g. api/vi/settings/deleteAccount
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class SettingsController {

    private final SettingsService settingsService;

    @DeleteMapping(path = "deleteAccount")
    public ResponseEntity<String> deleteAccount(@RequestBody DeleteAccountRequest deleteAccountRequest) {
        settingsService.deleteAccount(deleteAccountRequest);
        return ResponseEntity.ok("Account deleted successfully.");
    }
}