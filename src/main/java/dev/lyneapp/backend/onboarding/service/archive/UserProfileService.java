package dev.lyneapp.backend.onboarding.service.archive;

/**
 *
public class UserProfileService {

    public void yourName(YourNameRequest yourNameRequest) {
        Optional<User> user = userRepository.findByPhoneNumber(yourNameRequest.getPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setFirstName(yourNameRequest.getFirstName());
        verifiedUser.setLastName(yourNameRequest.getLastName());
        userRepository.save(verifiedUser);
    }

    public void yourEmail(YourEmailRequest yourEmailRequest) {
        boolean isValidEmail = emailValidator.test(yourEmailRequest.getEmailAddress());

        if (!isValidEmail) {
            throw new InvalidPasswordException(ExceptionMessages.EMAIL_IS_INVALID);
        }

        Optional<User> user = userRepository.findByPhoneNumber(yourEmailRequest.getPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setEmailAddress(yourEmailRequest.getEmailAddress());
        userRepository.save(verifiedUser);
    }

    public void yourDateOfBirth(YourDateOfBirthRequest yourDateOfBirthRequest) {
        Optional<User> user = userRepository.findByPhoneNumber(yourDateOfBirthRequest.getPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setDateOfBirth(yourDateOfBirthRequest.getDateOfBirth());
        userRepository.save(verifiedUser);
    }

    public void yourLocation(YourLocationRequest yourLocationRequest) {
        Optional<User> user = userRepository.findByPhoneNumber(yourLocationRequest.getPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setLocation(yourLocationRequest.getLocation());
        userRepository.save(verifiedUser);
    }

    public void yourPlaceOfBirth(YourPlaceOfBirthRequest yourPlaceOfBirthRequest) {
        Optional<User> user = userRepository.findByPhoneNumber(yourPlaceOfBirthRequest.getPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setLocation(yourPlaceOfBirthRequest.getLocation());
        userRepository.save(verifiedUser);
    }

    public void yourGender(YourGenderRequest yourGenderRequest) {
        Optional<User> user = userRepository.findByPhoneNumber(yourGenderRequest.getPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setGender(yourGenderRequest.getGender());
        userRepository.save(verifiedUser);
    }

    public void yourHeight(YourHeightRequest yourHeightRequest) {
        Optional<User> user = userRepository.findByPhoneNumber(yourHeightRequest.getPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setHeight(yourHeightRequest.getHeight());
        userRepository.save(verifiedUser);
    }

    public void yourTribe(YourTribeRequest yourTribeRequest) {
        Optional<User> user = userRepository.findByPhoneNumber(yourTribeRequest.getPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setTribe(yourTribeRequest.getTribe());
        userRepository.save(verifiedUser);
    }

    public void doYouDrink(DoYouDrinkRequest doYouDrinkRequest) {
        Optional<User> user = userRepository.findByPhoneNumber(doYouDrinkRequest.getPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setDoYouDrink(doYouDrinkRequest.isDoYouDrink());
        userRepository.save(verifiedUser);
    }

    public void doYouSmoke(DoYouSmokeRequest doYouSmokeRequest) {
        Optional<User> user = userRepository.findByPhoneNumber(doYouSmokeRequest.getPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setDoYouSmoke(doYouSmokeRequest.isDoYouSmoke());
        userRepository.save(verifiedUser);
    }

    public void yourReligion(YourReligionRequest yourReligionRequest) {
        Optional<User> user = userRepository.findByPhoneNumber(yourReligionRequest.getPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setReligion(yourReligionRequest.getReligion());
        userRepository.save(verifiedUser);
    }

    public void yourChildren(YourChildrenRequest yourChildrenRequest) {
        Optional<User> user = userRepository.findByPhoneNumber(yourChildrenRequest.getPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setDoYouHaveChildren(yourChildrenRequest.isDoYouHaveChildren());
        userRepository.save(verifiedUser);
    }

    public void yourOccupation(YourOccupationRequest yourOccupationRequest) {
        Optional<User> user = userRepository.findByPhoneNumber(yourOccupationRequest.getPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setOccupation(yourOccupationRequest.getOccupation());
        userRepository.save(verifiedUser);
    }

    public void yourQualification(YourQualificationRequest yourQualificationRequest) {
        Optional<User> user = userRepository.findByPhoneNumber(yourQualificationRequest.getPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setQualification(yourQualificationRequest.getQualification());
        userRepository.save(verifiedUser);
    }

    public void yourLanguage(YourLanguagesRequest yourLanguagesRequest) {
        Optional<User> user = userRepository.findByPhoneNumber(yourLanguagesRequest.getPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setLanguages(yourLanguagesRequest.getYourlanguages());
        userRepository.save(verifiedUser);
    }

    public void yourInterests(YourInterestsRequest yourInterestsRequest) {
        Optional<User> user = userRepository.findByPhoneNumber(yourInterestsRequest.getPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setInterests(yourInterestsRequest.getInterests());
        userRepository.save(verifiedUser);
    }

    public void yourAbout(YourAboutRequest yourAboutRequest) {
        Optional<User> user = userRepository.findByPhoneNumber(yourAboutRequest.getPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setAboutYou(yourAboutRequest.getAboutYou());
        userRepository.save(verifiedUser);
    }

    public void yourPrompts(YourPromptsRequest yourPromptsRequest) {
        Optional<User> user = userRepository.findByPhoneNumber(yourPromptsRequest.getPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        verifiedUser.setPrompts(yourPromptsRequest.getPrompts());
        userRepository.save(verifiedUser);
    }
}

 */
