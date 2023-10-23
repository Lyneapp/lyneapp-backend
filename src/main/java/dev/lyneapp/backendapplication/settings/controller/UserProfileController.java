package dev.lyneapp.backendapplication.settings.controller;

import dev.lyneapp.backendapplication.common.model.UserProfile;
import dev.lyneapp.backendapplication.settings.model.GetProfileRequest;
import dev.lyneapp.backendapplication.settings.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/settings/profile/")
@RequiredArgsConstructor
public class UserProfileController {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);

    private final UserProfileService userProfileService;
    @GetMapping("get")
    public ResponseEntity<UserProfile> getUserProfile(@Valid @RequestBody GetProfileRequest getProfileRequest) {
        LOGGER.info("Get profile request received for user with id: {}", getProfileRequest.getUserPhoneNumber());
        UserProfile userProfile = userProfileService.getUserProfile(getProfileRequest);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("adjust")
    public ResponseEntity<String> adjustField(@RequestParam String field, @Valid @RequestBody UserProfile userProfile) {
        LOGGER.info("Adjust {} request received for user with id: {}", field, userProfile.getUserPhoneNumber());

        switch (field) {
            case "prompt" -> {
                userProfileService.adjustPrompt(userProfile);
                return ResponseEntity.ok("Prompt adjusted successfully.");
            }
            case "about" -> {
                userProfileService.adjustAbout(userProfile);
                return ResponseEntity.ok("About adjusted successfully.");
            }
            case "language" -> {
                userProfileService.adjustLanguage(userProfile);
                return ResponseEntity.ok("Language adjusted successfully.");
            }
            case "dob" -> {
                userProfileService.adjustDOB(userProfile);
                return ResponseEntity.ok("Date of birth adjusted successfully.");
            }
            case "pob" -> {
                userProfileService.adjustPOB(userProfile);
                return ResponseEntity.ok("Place of birth adjusted successfully.");
            }
            case "location" -> {
                userProfileService.adjustLocation(userProfile);
                return ResponseEntity.ok("Location adjusted successfully.");
            }
            case "gender" -> {
                userProfileService.adjustGender(userProfile);
                return ResponseEntity.ok("Gender adjusted successfully.");
            }
            case "height" -> {
                userProfileService.adjustHeight(userProfile);
                return ResponseEntity.ok("Height adjusted successfully.");
            }
            case "tribe" -> {
                userProfileService.adjustTribe(userProfile);
                return ResponseEntity.ok("Tribe adjusted successfully.");
            }
            case "religion" -> {
                userProfileService.adjustReligion(userProfile);
                return ResponseEntity.ok("Religion adjusted successfully.");
            }
            case "drink" -> {
                userProfileService.adjustDrink(userProfile);
                return ResponseEntity.ok("Drink adjusted successfully.");
            }
            case "smoke" -> {
                userProfileService.adjustSmoke(userProfile);
                return ResponseEntity.ok("Smoke adjusted successfully.");
            }
            case "children" -> {
                userProfileService.adjustChildren(userProfile);
                return ResponseEntity.ok("Children adjusted successfully.");
            }
            case "qualification" -> {
                userProfileService.adjustQualification(userProfile);
                return ResponseEntity.ok("Qualification adjusted successfully.");
            }
            case "occupation" -> {
                userProfileService.adjustOccupation(userProfile);
                return ResponseEntity.ok("Occupation adjusted successfully.");
            }
            case "interest" -> {
                userProfileService.adjustInterest(userProfile);
                return ResponseEntity.ok("Interest adjusted successfully.");
            }
            default -> {
                return ResponseEntity.badRequest().body("Invalid field.");
            }
        }
    }


    @PutMapping("adjustPrompt")
    public ResponseEntity<String> adjustPrompt(@Valid @RequestBody UserProfile userProfile) {
        LOGGER.info("Adjust prompt request received for user with id: {}", userProfile.getUserPhoneNumber());
        userProfileService.adjustPrompt(userProfile);
        return ResponseEntity.ok("Prompt adjusted successfully.");
    }


    @PutMapping("adjustAbout")
    public ResponseEntity<String> adjustAbout(@Valid @RequestBody UserProfile userProfile) {
        LOGGER.info("Adjust about request received for user with id: {}", userProfile.getUserPhoneNumber());
        userProfileService.adjustAbout(userProfile);
        return ResponseEntity.ok("About adjusted successfully.");
    }


    @PutMapping("adjustLanguage")
    public ResponseEntity<String> adjustLanguage(UserProfile userProfile) {
        LOGGER.info("Adjust language request received for user with id: {}", userProfile.getUserPhoneNumber());
        userProfileService.adjustLanguage(userProfile);
        return ResponseEntity.ok("Language adjusted successfully.");
    }


    @PutMapping("adjustDOB")
    public ResponseEntity<String> adjustDOB(UserProfile userProfile) {
        LOGGER.info("Adjust date of birth request received for user with id: {}", userProfile.getUserPhoneNumber());
        userProfileService.adjustDOB(userProfile);
        return ResponseEntity.ok("Date of birth adjusted successfully.");
    }


    @PutMapping("adjustPOB")
    public ResponseEntity<String> adjustPOB(UserProfile userProfile) {
        LOGGER.info("Adjust place of birth request received for user with id: {}", userProfile.getUserPhoneNumber());
        userProfileService.adjustPOB(userProfile);
        return ResponseEntity.ok("Place of birth adjusted successfully.");
    }


    @PutMapping("adjustLocation")
    public ResponseEntity<String> adjustLocation(UserProfile userProfile) {
        LOGGER.info("Adjust location request received for user with id: {}", userProfile.getUserPhoneNumber());
        userProfileService.adjustLocation(userProfile);
        return ResponseEntity.ok("Location adjusted successfully.");
    }


    @PutMapping("adjustGender")
    public ResponseEntity<String> adjustGender(UserProfile userProfile) {
        LOGGER.info("Adjust gender request received for user with id: {}", userProfile.getUserPhoneNumber());
        userProfileService.adjustGender(userProfile);
        return ResponseEntity.ok("Gender adjusted successfully.");
    }


    @PutMapping("adjustHeight")
    public ResponseEntity<String> adjustHeight(UserProfile userProfile) {
        LOGGER.info("Adjust height request received for user with id: {}", userProfile.getUserPhoneNumber());
        userProfileService.adjustHeight(userProfile);
        return ResponseEntity.ok("Height adjusted successfully.");
    }


    @PutMapping("adjustTribe")
    public ResponseEntity<String> adjustTribe(UserProfile userProfile) {
        LOGGER.info("Adjust tribe request received for user with id: {}", userProfile.getUserPhoneNumber());
        userProfileService.adjustTribe(userProfile);
        return ResponseEntity.ok("Tribe adjusted successfully.");
    }


    @PutMapping("adjustReligion")
    public ResponseEntity<String> adjustReligion(UserProfile userProfile) {
        LOGGER.info("Adjust religion request received for user with id: {}", userProfile.getUserPhoneNumber());
        userProfileService.adjustReligion(userProfile);
        return ResponseEntity.ok("Religion adjusted successfully.");
    }


    @PutMapping("adjustDrink")
    public ResponseEntity<String> adjustDrink(UserProfile userProfile) {
        LOGGER.info("Adjust drink request received for user with id: {}", userProfile.getUserPhoneNumber());
        userProfileService.adjustDrink(userProfile);
        return ResponseEntity.ok("Drink adjusted successfully.");
    }


    @PutMapping("adjustSmoke")
    public ResponseEntity<String> adjustSmoke(UserProfile userProfile) {
        LOGGER.info("Adjust smoke request received for user with id: {}", userProfile.getUserPhoneNumber());
        userProfileService.adjustSmoke(userProfile);
        return ResponseEntity.ok("Smoke adjusted successfully.");
    }

    @PutMapping("adjustChildren")
    public ResponseEntity<String> adjustChildren(UserProfile userProfile) {
        LOGGER.info("Adjust children request received for user with id: {}", userProfile.getUserPhoneNumber());
        userProfileService.adjustChildren(userProfile);
        return ResponseEntity.ok("Children adjusted successfully.");
    }

    @PutMapping("adjustQualification")
    public ResponseEntity<String> adjustQualification(UserProfile userProfile) {
        LOGGER.info("Adjust qualification request received for user with id: {}", userProfile.getUserPhoneNumber());
        userProfileService.adjustQualification(userProfile);
        return ResponseEntity.ok("Qualification adjusted successfully.");
    }

    @PutMapping("adjustOccupation")
    public ResponseEntity<String> adjustOccupation(UserProfile userProfile) {
        LOGGER.info("Adjust occupation request received for user with id: {}", userProfile.getUserPhoneNumber());
        userProfileService.adjustOccupation(userProfile);
        return ResponseEntity.ok("Qualification adjusted successfully.");
    }

    @PutMapping("adjustInterest")
    public ResponseEntity<String> adjustInterest(UserProfile userProfile) {
        LOGGER.info("Adjust interest request received for user with id: {}", userProfile.getUserPhoneNumber());
        userProfileService.adjustInterest(userProfile);
        return ResponseEntity.ok("Qualification adjusted successfully.");
    }
}
