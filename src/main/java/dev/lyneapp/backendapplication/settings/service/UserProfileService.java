package dev.lyneapp.backendapplication.settings.service;


import dev.lyneapp.backendapplication.settings.model.GetProfileRequest;
import dev.lyneapp.backendapplication.common.model.User;
import dev.lyneapp.backendapplication.common.model.UserProfile;
import dev.lyneapp.backendapplication.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static dev.lyneapp.backendapplication.common.util.Validation.verifyPhoneNumberExist;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;

    private final static Logger LOGGER = LoggerFactory.getLogger(UserProfileService.class);

    public UserProfile getUserProfile(GetProfileRequest getProfileRequest) {
        LOGGER.info("Entering UserProfileService.getUserProfile ,Getting user profile: {}", getProfileRequest);
        Optional<User> user = userRepository.findByUserPhoneNumber(getProfileRequest.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);
        
        UserProfile userProfile = new UserProfile();
        Optional.ofNullable(verifiedUser.getId()).ifPresent(userProfile::setId);
        Optional.ofNullable(verifiedUser.getUserPhoneNumber()).ifPresent(userProfile::setUserPhoneNumber);
        Optional.ofNullable(verifiedUser.getFirstName()).ifPresent(userProfile::setFirstName);
        Optional.ofNullable(verifiedUser.getLastName()).ifPresent(userProfile::setLastName);
        Optional.ofNullable(verifiedUser.getAge()).ifPresent(userProfile::setAge);
        Optional.ofNullable(verifiedUser.getMediaFileURLs()).ifPresent(userProfile::setMediaFileURLs);
        Optional.ofNullable(verifiedUser.getTribe()).ifPresent(userProfile::setTribe);
        Optional.ofNullable(verifiedUser.getReligion()).ifPresent(userProfile::setReligion);
        Optional.ofNullable(verifiedUser.getGender()).ifPresent(userProfile::setGender);
        Optional.ofNullable(verifiedUser.getAboutYou()).ifPresent(userProfile::setAboutUser);
        Optional.ofNullable(verifiedUser.getHeight()).ifPresent(userProfile::setHeight);
        Optional.ofNullable(verifiedUser.getJob()).ifPresent(userProfile::setJob);
        Optional.ofNullable(verifiedUser.getIndustry()).ifPresent(userProfile::setIndustry);
        Optional.ofNullable(verifiedUser.getInstitutionName()).ifPresent(userProfile::setInstitutionName);
        Optional.ofNullable(verifiedUser.getHighestDegree()).ifPresent(userProfile::setHighestDegree);
        Optional.ofNullable(verifiedUser.getCurrentLocation()).ifPresent(userProfile::setCurrentLocation);
        Optional.ofNullable(verifiedUser.getLanguages()).ifPresent(userProfile::setLanguages);
        Optional.ofNullable(verifiedUser.getInterests()).ifPresent(userProfile::setInterests);
        Optional.ofNullable(verifiedUser.getPrompts()).ifPresent(userProfile::setPrompts);
        Optional.of(verifiedUser.getRemainingBouquets()).ifPresent(userProfile::setRemainingBouquets);
        Optional.of(verifiedUser.isDoYouHaveChildren()).ifPresent(userProfile::setDoYouHaveChildren);
        Optional.of(verifiedUser.isDoYouDrink()).ifPresent(userProfile::setDoYouDrink);
        Optional.of(verifiedUser.isDoYouSmoke()).ifPresent(userProfile::setDoYouSmoke);

        LOGGER.info("Exiting UserProfileService.getUserProfile ,User profile: {}", userProfile);
        return userProfile;
    }


    public void adjustField(String field, UserProfile userProfile) {
        LOGGER.info("Entering UserProfileService.adjustField ,Adjusting {}: {}", field, userProfile);
        Optional<User> user = userRepository.findByUserPhoneNumber(userProfile.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        switch (field) {
            case "prompt" -> verifiedUser.setPrompts(userProfile.getPrompts());
            case "about" -> verifiedUser.setAboutYou(userProfile.getAboutUser());
            case "language" -> verifiedUser.setLanguages(userProfile.getLanguages());
            case "dob" -> verifiedUser.setAge(userProfile.getDateOfBirth());
            case "pob" -> verifiedUser.setCurrentLocation(userProfile.getPlaceOfBirth());
            case "location" -> verifiedUser.setCurrentLocation(userProfile.getCurrentLocation());
            case "gender" -> verifiedUser.setGender(userProfile.getGender());
            case "height" -> verifiedUser.setHeight(userProfile.getHeight());
            case "tribe" -> verifiedUser.setTribe(userProfile.getTribe());
            case "religion" -> verifiedUser.setReligion(userProfile.getReligion());
            case "drink" -> verifiedUser.setDoYouDrink(userProfile.isDoYouDrink());
            case "smoke" -> verifiedUser.setDoYouSmoke(userProfile.isDoYouSmoke());
            case "children" -> verifiedUser.setDoYouHaveChildren(userProfile.isDoYouHaveChildren());
            case "qualification" -> {
                verifiedUser.setInstitutionName(userProfile.getInstitutionName());
                verifiedUser.setHighestDegree(userProfile.getHighestDegree());
            }
            case "occupation" -> {
                verifiedUser.setIndustry(userProfile.getIndustry());
                verifiedUser.setJob(userProfile.getJob());
            }
            case "interest" -> verifiedUser.setInterests(userProfile.getInterests());
            default -> throw new IllegalArgumentException("Invalid field.");
        }

        userRepository.save(verifiedUser);
        LOGGER.info("Exiting UserProfileService.adjustField ,{} adjusted successfully.", field);
    }


    public void adjustPrompt(UserProfile userProfile) {
        LOGGER.info("Entering UserProfileService.adjustPrompt ,Adjusting prompt: {}", userProfile);
        Optional<User> user = userRepository.findByUserPhoneNumber(userProfile.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);
        verifiedUser.setPrompts(userProfile.getPrompts());
        userRepository.save(verifiedUser);
        LOGGER.info("Exiting UserProfileService.adjustPrompt ,Prompt adjusted successfully.");
    }


    public void adjustAbout(UserProfile userProfile) {
        LOGGER.info("Entering UserProfileService.adjustAbout ,Adjusting about: {}", userProfile);
        Optional<User> user = userRepository.findByUserPhoneNumber(userProfile.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);
        verifiedUser.setAboutYou(userProfile.getAboutUser());
        userRepository.save(verifiedUser);
        LOGGER.info("Exiting UserProfileService.adjustAbout ,About adjusted successfully.");
    }

    public void adjustLanguage(UserProfile userProfile) {
        LOGGER.info("Entering UserProfileService.adjustLanguage ,Adjusting language: {}", userProfile);
        Optional<User> user = userRepository.findByUserPhoneNumber(userProfile.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);
        verifiedUser.setLanguages(userProfile.getLanguages());
        userRepository.save(verifiedUser);
        LOGGER.info("Exiting UserProfileService.adjustLanguage ,Language adjusted successfully.");
    }

    public void adjustDOB(UserProfile userProfile) {
        LOGGER.info("Entering UserProfileService.adjustDOB ,Adjusting DOB: {}", userProfile);
        Optional<User> user = userRepository.findByUserPhoneNumber(userProfile.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);
        verifiedUser.setAge(userProfile.getDateOfBirth());
        userRepository.save(verifiedUser);
        LOGGER.info("Exiting UserProfileService.adjustDOB ,DOB adjusted successfully.");
    }

    public void adjustPOB(UserProfile userProfile) {
        LOGGER.info("Entering UserProfileService.adjustPOB ,Adjusting POB: {}", userProfile);
        Optional<User> user = userRepository.findByUserPhoneNumber(userProfile.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);
        verifiedUser.setCurrentLocation(userProfile.getPlaceOfBirth());
        userRepository.save(verifiedUser);
        LOGGER.info("Exiting UserProfileService.adjustPOB ,POB adjusted successfully.");
    }

    public void adjustLocation(UserProfile userProfile) {
        LOGGER.info("Entering UserProfileService.adjustLocation ,Adjusting location: {}", userProfile);
        Optional<User> user = userRepository.findByUserPhoneNumber(userProfile.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);
        verifiedUser.setCurrentLocation(userProfile.getCurrentLocation());
        userRepository.save(verifiedUser);
        LOGGER.info("Exiting UserProfileService.adjustLocation ,Location adjusted successfully.");
    }

    public void adjustGender(UserProfile userProfile) {
        LOGGER.info("Entering UserProfileService.adjustGender ,Adjusting location: {}", userProfile);
        Optional<User> user = userRepository.findByUserPhoneNumber(userProfile.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);
        verifiedUser.setGender(userProfile.getGender());
        userRepository.save(verifiedUser);
        LOGGER.info("Exiting UserProfileService.adjustGender ,Location adjusted successfully.");
    }

    public void adjustHeight(UserProfile userProfile) {
        LOGGER.info("Entering UserProfileService.adjustHeight ,Adjusting height: {}", userProfile);
        Optional<User> user = userRepository.findByUserPhoneNumber(userProfile.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);
        verifiedUser.setHeight(userProfile.getHeight());
        userRepository.save(verifiedUser);
        LOGGER.info("Exiting UserProfileService.adjustHeight ,Height adjusted successfully.");
    }

    public void adjustTribe(UserProfile userProfile) {
        LOGGER.info("Entering UserProfileService.adjustTribe ,Adjusting tribe: {}", userProfile);
        Optional<User> user = userRepository.findByUserPhoneNumber(userProfile.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);
        verifiedUser.setTribe(userProfile.getTribe());
        userRepository.save(verifiedUser);
        LOGGER.info("Exiting UserProfileService.adjustTribe ,Tribe adjusted successfully.");
    }

    public void adjustReligion(UserProfile userProfile) {
        LOGGER.info("Entering UserProfileService.adjustReligion ,Adjusting religion: {}", userProfile);
        Optional<User> user = userRepository.findByUserPhoneNumber(userProfile.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);
        verifiedUser.setReligion(userProfile.getReligion());
        userRepository.save(verifiedUser);
        LOGGER.info("Exiting UserProfileService.adjustReligion ,Religion adjusted successfully.");
    }

    public void adjustDrink(UserProfile userProfile) {
        LOGGER.info("Entering UserProfileService.adjustDrink ,Adjusting drink: {}", userProfile);
        Optional<User> user = userRepository.findByUserPhoneNumber(userProfile.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);
        verifiedUser.setDoYouDrink(userProfile.isDoYouDrink());
        userRepository.save(verifiedUser);
        LOGGER.info("Exiting UserProfileService.adjustDrink ,Drink adjusted successfully.");
    }

    public void adjustSmoke(UserProfile userProfile) {
        LOGGER.info("Entering UserProfileService.adjustSmoke ,Adjusting smoke: {}", userProfile);
        Optional<User> user = userRepository.findByUserPhoneNumber(userProfile.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);
        verifiedUser.setDoYouSmoke(userProfile.isDoYouSmoke());
        userRepository.save(verifiedUser);
        LOGGER.info("Exiting UserProfileService.adjustSmoke ,Smoke adjusted successfully.");
    }

    public void adjustChildren(UserProfile userProfile) {
        LOGGER.info("Entering UserProfileService.adjustChildren ,Adjusting children: {}", userProfile);
        Optional<User> user = userRepository.findByUserPhoneNumber(userProfile.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);
        verifiedUser.setDoYouHaveChildren(userProfile.isDoYouHaveChildren());
        userRepository.save(verifiedUser);
        LOGGER.info("Exiting UserProfileService.adjustChildren ,Children adjusted successfully.");
    }

    public void adjustQualification(UserProfile userProfile) {
        LOGGER.info("Entering UserProfileService.adjustQualification ,Adjusting qualification: {}", userProfile);
        Optional<User> user = userRepository.findByUserPhoneNumber(userProfile.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);
        verifiedUser.setInstitutionName(userProfile.getInstitutionName());
        verifiedUser.setHighestDegree(userProfile.getHighestDegree());
        userRepository.save(verifiedUser);
        LOGGER.info("Exiting UserProfileService.adjustQualification ,Qualification adjusted successfully.");
    }

    public void adjustOccupation(UserProfile userProfile) {
        LOGGER.info("Entering UserProfileService.adjustOccupation ,Adjusting occupation: {}", userProfile);
        Optional<User> user = userRepository.findByUserPhoneNumber(userProfile.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);
        verifiedUser.setIndustry(userProfile.getIndustry());
        verifiedUser.setJob(userProfile.getJob());
        userRepository.save(verifiedUser);
        LOGGER.info("Exiting UserProfileService.adjustOccupation ,Occupation adjusted successfully.");
    }

    public void adjustInterest(UserProfile userProfile) {
        LOGGER.info("Entering UserProfileService.adjustInterest ,Adjusting interest: {}", userProfile);
        Optional<User> user = userRepository.findByUserPhoneNumber(userProfile.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);
        verifiedUser.setInterests(userProfile.getInterests());
        userRepository.save(verifiedUser);
        LOGGER.info("Exiting UserProfileService.adjustInterest ,Interest adjusted successfully.");
    }
}