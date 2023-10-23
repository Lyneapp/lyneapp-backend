package dev.lyneapp.backendapplication.settings.controller;


import dev.lyneapp.backendapplication.common.model.PhoneNumberDTO;
import dev.lyneapp.backendapplication.onboarding.model.request.PreferenceDTO;
import dev.lyneapp.backendapplication.settings.service.AdjustPreferencesService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/settings/adjustPreferences/")
@RequiredArgsConstructor
public class AdjustPreferencesController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SettingsController.class);

    private final AdjustPreferencesService adjustPreferencesService;

    //get all preferences
    @GetMapping("all")
    public ResponseEntity<PreferenceDTO> getAllPreferences(PhoneNumberDTO phoneNumberRequest) {
        LOGGER.info("Get all preferences request received for user with id: {}", phoneNumberRequest.getUserPhoneNumber());
        PreferenceDTO preferenceDTO = adjustPreferencesService.getAllPreferences(phoneNumberRequest);
        return ResponseEntity.ok(preferenceDTO);
    }

    @PutMapping("adjustPreferences")
    public ResponseEntity<String> adjustPreferences(@RequestBody PreferenceDTO preferenceRequest) {
        LOGGER.info("Adjust preferences request received for user with id: {}", preferenceRequest.getUserPhoneNumber());
        adjustPreferencesService.adjustPreferences(preferenceRequest);
        return ResponseEntity.ok("Preferences adjusted successfully.");
    }


    //heightRange
    @PutMapping("heightRange")
    public ResponseEntity<String> adjustHeightRange(PreferenceDTO preferenceRequest) {
        LOGGER.info("Adjust height range request received for user with id: {}", preferenceRequest.getUserPhoneNumber());
        adjustPreferencesService.adjustHeightRange(preferenceRequest);
        return ResponseEntity.ok("Height range adjusted successfully.");
    }

    //tribe
    @PutMapping("tribe")
    public ResponseEntity<String> adjustTribe(PreferenceDTO preferenceRequest) {
        LOGGER.info("Adjust tribe request received for user with id: {}", preferenceRequest.getUserPhoneNumber());
        adjustPreferencesService.adjustTribe(preferenceRequest);
        return ResponseEntity.ok("Tribe adjusted successfully.");
    }

    //education level
    @PutMapping("qualification")
    public ResponseEntity<String> adjustQualification(PreferenceDTO preferenceRequest) {
        LOGGER.info("Adjust qualification request received for user with id: {}", preferenceRequest.getUserPhoneNumber());
        adjustPreferencesService.adjustQualification(preferenceRequest);
        return ResponseEntity.ok("Qualification adjusted successfully.");
    }

    //drinking
    @PutMapping("drinking")
    public ResponseEntity<String> adjustDrinking(PreferenceDTO preferenceRequest) {
        LOGGER.info("Adjust drinking request received for user with id: {}", preferenceRequest.getUserPhoneNumber());
        adjustPreferencesService.adjustDrinking(preferenceRequest);
        return ResponseEntity.ok("Drinking adjusted successfully.");
    }

    //smoking
    @PutMapping("smoking")
    public ResponseEntity<String> adjustSmoking(PreferenceDTO preferenceRequest) {
        LOGGER.info("Adjust smoking request received for user with id: {}", preferenceRequest.getUserPhoneNumber());
        adjustPreferencesService.adjustSmoking(preferenceRequest);
        return ResponseEntity.ok("Smoking adjusted successfully.");
    }

    //children
    @PutMapping("children")
    public ResponseEntity<String> adjustChildren(PreferenceDTO preferenceRequest) {
        LOGGER.info("Adjust children request received for user with id: {}", preferenceRequest.getUserPhoneNumber());
        adjustPreferencesService.adjustChildren(preferenceRequest);
        return ResponseEntity.ok("children adjusted successfully.");
    }

    //languages
    @PutMapping("languages")
    public ResponseEntity<String> adjustLanguages(PreferenceDTO preferenceRequest) {
        LOGGER.info("Adjust languages request received for user with id: {}", preferenceRequest.getUserPhoneNumber());
        adjustPreferencesService.adjustLanguages(preferenceRequest);
        return ResponseEntity.ok("languages adjusted successfully.");
    }

    //occupation
    @PutMapping("occupation")
    public ResponseEntity<String> adjustOccupation(PreferenceDTO preferenceRequest) {
        LOGGER.info("Adjust occupation request received for user with id: {}", preferenceRequest.getUserPhoneNumber());
        adjustPreferencesService.adjustOccupation(preferenceRequest);
        return ResponseEntity.ok("occupation adjusted successfully.");
    }

    //AgeRange
    @PutMapping("ageRange")
    public ResponseEntity<String> adjustAgeRange(PreferenceDTO preferenceRequest) {
        LOGGER.info("Adjust age range request received for user with id: {}", preferenceRequest.getUserPhoneNumber());
        adjustPreferencesService.adjustAgeRange(preferenceRequest);
        return ResponseEntity.ok("age range adjusted successfully.");
    }

    //location
    @PutMapping("location")
    public ResponseEntity<String> adjustLocation(PreferenceDTO preferenceRequest) {
        LOGGER.info("Adjust location request received for user with id: {}", preferenceRequest.getUserPhoneNumber());
        adjustPreferencesService.adjustLocation(preferenceRequest);
        return ResponseEntity.ok("location adjusted successfully.");
    }

    //religion
    @PutMapping("religion")
    public ResponseEntity<String> adjustReligion(PreferenceDTO preferenceRequest) {
        LOGGER.info("religion request received for user with id: {}", preferenceRequest.getUserPhoneNumber());
        adjustPreferencesService.adjustReligion(preferenceRequest);
        return ResponseEntity.ok("religion adjusted successfully.");
    }
}

