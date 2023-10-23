package dev.lyneapp.backendapplication.settings.service;


import dev.lyneapp.backendapplication.common.model.UserPreference;
import dev.lyneapp.backendapplication.common.repository.UserPreferenceRepository;
import dev.lyneapp.backendapplication.common.model.PhoneNumberDTO;
import dev.lyneapp.backendapplication.onboarding.model.request.PreferenceDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

// FIXME: Updated/Adjusting the preferences should trigger a new recommendation set in the datastore (cache/elastic/memory/db)

@Service
@RequiredArgsConstructor
public class AdjustPreferencesService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AdjustPreferencesService.class);

    private final UserPreferenceRepository userPreferenceRepository;

    //get all preferences
    public PreferenceDTO getAllPreferences(PhoneNumberDTO phoneNumberDTO) {
        LOGGER.info("Entering AdjustPreferencesService.getAllPreferences");
        UserPreference userPreference = userPreferenceRepository.findByUserPhoneNumber(phoneNumberDTO.getUserPhoneNumber()).orElse(null);
        assert userPreference != null;

//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//        PreferenceDTO preferenceDTO = modelMapper.map(userPreference, PreferenceDTO.class);

        PreferenceDTO preferenceDTO = PreferenceDTO.builder()
                .preferredHeightRange(userPreference.getPreferredHeightRange())
                .preferredAgeRange(userPreference.getPreferredAgeRange())
                .preferredTribes(userPreference.getPreferredTribes())
                .preferredQualification(userPreference.getPreferredQualification())
                .preferredOccupation(userPreference.getPreferredOccupation())
                .shouldTheyDrink(userPreference.isShouldTheyDrink())
                .shouldTheySmoke(userPreference.isShouldTheySmoke())
                .shouldTheyHaveChildren(userPreference.isShouldTheyHaveChildren())
                .doYouWantSomeoneWithChildren(userPreference.getDoYouWantSomeoneWithChildren())
                .preferredGender(userPreference.getPreferredGender())
                .preferredInterests(userPreference.getPreferredInterests())
                .preferredLocations(userPreference.getPreferredLocations())
                .preferredLanguages(userPreference.getPreferredLanguages())
                .preferredReligion(userPreference.getPreferredReligion())
                .preferenceUpdated(userPreference.isPreferenceUpdated())
                .preferenceUpdatedAt(userPreference.getPreferenceUpdatedAt())
                .preferenceUpdatedAt(userPreference.getPreferenceUpdatedAt())
                .build();
        LOGGER.info("Exiting AdjustPreferencesService.getAllPreferences");
        return preferenceDTO;
    }

    public void adjustPreferences(PreferenceDTO preferenceRequest) {
        LOGGER.info("Entering AdjustPreferencesService.adjustPreferences");
        UserPreference userPreference = userPreferenceRepository.findByUserPhoneNumber(preferenceRequest.getUserPhoneNumber()).orElse(null);
        assert userPreference != null;

        // Update preference properties
        userPreference.setPreferredHeightRange(preferenceRequest.getPreferredHeightRange());
        userPreference.setPreferredTribes(preferenceRequest.getPreferredTribes());
        userPreference.setPreferredQualification(preferenceRequest.getPreferredQualification());
        userPreference.setShouldTheyDrink(preferenceRequest.isShouldTheyDrink());
        userPreference.setShouldTheySmoke(preferenceRequest.isShouldTheySmoke());
        userPreference.setShouldTheyHaveChildren(preferenceRequest.isShouldTheyHaveChildren());
        userPreference.setDoYouWantSomeoneWithChildren(preferenceRequest.getDoYouWantSomeoneWithChildren());
        userPreference.setPreferredLanguages(preferenceRequest.getPreferredLanguages());
        userPreference.setPreferredOccupation(preferenceRequest.getPreferredOccupation());
        userPreference.setPreferredAgeRange(preferenceRequest.getPreferredAgeRange());
        userPreference.setPreferredLocations(preferenceRequest.getPreferredLocations());
        userPreference.setPreferredReligion(preferenceRequest.getPreferredReligion());

        userPreference.setPreferenceUpdatedAt(LocalDateTime.now());
        userPreference.setPreferenceUpdated(true);
        userPreferenceRepository.save(userPreference);

        LOGGER.info("Exiting AdjustPreferencesService.adjustPreferences");
    }

    //height range
    public void adjustHeightRange(PreferenceDTO preferenceRequest) {
        LOGGER.info("Entering AdjustPreferencesService.adjustHeightRange");
        UserPreference userPreference = userPreferenceRepository.findByUserPhoneNumber(preferenceRequest.getUserPhoneNumber()).orElse(null);
        assert userPreference != null;
        userPreference.setPreferredHeightRange(preferenceRequest.getPreferredHeightRange());
        userPreference.setPreferenceUpdatedAt(LocalDateTime.now());
        userPreference.setPreferenceUpdated(true);
        userPreferenceRepository.save(userPreference);
        LOGGER.info("Exiting AdjustPreferencesService.adjustHeightRange");
    }

    //tribe
    public void adjustTribe(PreferenceDTO preferenceRequest) {
        LOGGER.info("Entering AdjustPreferencesService.adjustTribe");
        UserPreference userPreference = userPreferenceRepository.findByUserPhoneNumber(preferenceRequest.getUserPhoneNumber()).orElse(null);
        assert userPreference != null;
        userPreference.setPreferredTribes(preferenceRequest.getPreferredTribes());
        userPreference.setPreferenceUpdatedAt(LocalDateTime.now());
        userPreference.setPreferenceUpdated(true);
        userPreferenceRepository.save(userPreference);
        LOGGER.info("Exiting AdjustPreferencesService.adjustTribe");
    }

    //education level
    public void adjustQualification(PreferenceDTO preferenceRequest) {
        LOGGER.info("Entering AdjustPreferencesService.adjustQualification");
        UserPreference userPreference = userPreferenceRepository.findByUserPhoneNumber(preferenceRequest.getUserPhoneNumber()).orElse(null);
        assert userPreference != null;
        userPreference.setPreferredQualification(preferenceRequest.getPreferredQualification());
        userPreference.setPreferenceUpdatedAt(LocalDateTime.now());
        userPreference.setPreferenceUpdated(true);
        userPreferenceRepository.save(userPreference);
        LOGGER.info("Exiting AdjustPreferencesService.adjustQualification");
    }

    //drinking
    public void adjustDrinking(PreferenceDTO preferenceRequest) {
        LOGGER.info("Entering AdjustPreferencesService.adjustDrinking");
        UserPreference userPreference = userPreferenceRepository.findByUserPhoneNumber(preferenceRequest.getUserPhoneNumber()).orElse(null);
        assert userPreference != null;
        userPreference.setShouldTheyDrink(preferenceRequest.isShouldTheyDrink());
        userPreference.setPreferenceUpdatedAt(LocalDateTime.now());
        userPreference.setPreferenceUpdated(true);
        userPreferenceRepository.save(userPreference);
        LOGGER.info("Exiting AdjustPreferencesService.adjustDrinking");
    }

    //smoking
    public void adjustSmoking(PreferenceDTO preferenceRequest) {
        LOGGER.info("Entering AdjustPreferencesService.adjustSmoking");
        UserPreference userPreference = userPreferenceRepository.findByUserPhoneNumber(preferenceRequest.getUserPhoneNumber()).orElse(null);
        assert userPreference != null;
        userPreference.setShouldTheySmoke(preferenceRequest.isShouldTheySmoke());
        userPreference.setPreferenceUpdatedAt(LocalDateTime.now());
        userPreference.setPreferenceUpdated(true);
        userPreferenceRepository.save(userPreference);
        LOGGER.info("Exiting AdjustPreferencesService.adjustSmoking");
    }

    //children
    public void adjustChildren(PreferenceDTO preferenceRequest) {
        LOGGER.info("Entering AdjustPreferencesService.adjustChildren");
        UserPreference userPreference = userPreferenceRepository.findByUserPhoneNumber(preferenceRequest.getUserPhoneNumber()).orElse(null);
        assert userPreference != null;
        userPreference.setShouldTheyHaveChildren(preferenceRequest.isShouldTheyHaveChildren());
        userPreference.setDoYouWantSomeoneWithChildren(preferenceRequest.getDoYouWantSomeoneWithChildren());
        userPreference.setPreferenceUpdatedAt(LocalDateTime.now());
        userPreference.setPreferenceUpdated(true);
        userPreferenceRepository.save(userPreference);
        LOGGER.info("Exiting AdjustPreferencesService.adjustChildren");
    }

    //languages
    public void adjustLanguages(PreferenceDTO preferenceRequest) {
        LOGGER.info("Entering AdjustPreferencesService.adjustLanguages");
        UserPreference userPreference = userPreferenceRepository.findByUserPhoneNumber(preferenceRequest.getUserPhoneNumber()).orElse(null);
        assert userPreference != null;
        userPreference.setPreferredLanguages(preferenceRequest.getPreferredLanguages());
        userPreference.setPreferenceUpdatedAt(LocalDateTime.now());
        userPreference.setPreferenceUpdated(true);
        userPreferenceRepository.save(userPreference);
        LOGGER.info("Exiting AdjustPreferencesService.adjustLanguages");
    }

    //occupation
    public void adjustOccupation(PreferenceDTO preferenceRequest) {
        LOGGER.info("Entering AdjustPreferencesService.adjustOccupation");
        UserPreference userPreference = userPreferenceRepository.findByUserPhoneNumber(preferenceRequest.getUserPhoneNumber()).orElse(null);
        assert userPreference != null;
        userPreference.setPreferredOccupation(preferenceRequest.getPreferredOccupation());
        userPreference.setPreferenceUpdatedAt(LocalDateTime.now());
        userPreference.setPreferenceUpdated(true);
        userPreferenceRepository.save(userPreference);
        LOGGER.info("Exiting AdjustPreferencesService.adjustOccupation");
    }

    //age range
    public void adjustAgeRange(PreferenceDTO preferenceRequest) {
        LOGGER.info("Entering AdjustPreferencesService.adjustAgeRange");
        UserPreference userPreference = userPreferenceRepository.findByUserPhoneNumber(preferenceRequest.getUserPhoneNumber()).orElse(null);
        assert userPreference != null;
        userPreference.setPreferredAgeRange(preferenceRequest.getPreferredAgeRange());
        userPreference.setPreferenceUpdatedAt(LocalDateTime.now());
        userPreference.setPreferenceUpdated(true);
        userPreferenceRepository.save(userPreference);
        LOGGER.info("Exiting AdjustPreferencesService.adjustAgeRange");
    }

    //location
    public void adjustLocation(PreferenceDTO preferenceRequest) {
        LOGGER.info("Entering AdjustPreferencesService.adjustLocation");
        UserPreference userPreference = userPreferenceRepository.findByUserPhoneNumber(preferenceRequest.getUserPhoneNumber()).orElse(null);
        assert userPreference != null;
        userPreference.setPreferredLocations(preferenceRequest.getPreferredLocations());
        userPreference.setPreferenceUpdatedAt(LocalDateTime.now());
        userPreference.setPreferenceUpdated(true);
        userPreferenceRepository.save(userPreference);
        LOGGER.info("Exiting AdjustPreferencesService.adjustLocation");
    }

    //religion
    public void adjustReligion(PreferenceDTO preferenceRequest) {
        LOGGER.info("Entering AdjustPreferencesService.adjustReligion");
        UserPreference userPreference = userPreferenceRepository.findByUserPhoneNumber(preferenceRequest.getUserPhoneNumber()).orElse(null);
        assert userPreference != null;
        userPreference.setPreferredReligion(preferenceRequest.getPreferredReligion());
        userPreference.setPreferenceUpdatedAt(LocalDateTime.now());
        userPreference.setPreferenceUpdated(true);
        userPreferenceRepository.save(userPreference);
        LOGGER.info("Exiting AdjustPreferencesService.adjustReligion");
    }
}
