package dev.lyneapp.backendapplication.settings.service;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.Image;
import dev.lyneapp.backendapplication.common.model.User;
import dev.lyneapp.backendapplication.common.repository.UserRepository;
import dev.lyneapp.backendapplication.common.util.exception.AccountNotVerifiedException;
import dev.lyneapp.backendapplication.settings.model.VerificationDTO;
import dev.lyneapp.backendapplication.settings.repository.VerificationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;

import static dev.lyneapp.backendapplication.common.util.Validation.verifyPhoneNumberExist;

// FIXME: Consider saving the image in s3 during the initial request and then get them and compare the image and the ID and flag account verified as true
// FIXME: This would help with future verification of images the user uploads to the app

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(VerificationService.class);
    private static final String USER_NOT_VERIFIED = "User not verified.";


    private final VerificationRequestRepository verificationRequestRepository;
    private final UserRepository userRepository;
    private final AmazonRekognition amazonRekognition;

    public void createVerificationRequest(VerificationDTO verificationRequest) {
        LOGGER.info("Creating verification request for user: {}", verificationRequest.getUserPhoneNumber());
        Optional<User> user = userRepository.findByUserPhoneNumber(verificationRequest.getUserPhoneNumber());
        User verifiedUser = verifyPhoneNumberExist(user);

        if (!verifiedUser.isAccountVerifiedBadge()) {
            throw new AccountNotVerifiedException(USER_NOT_VERIFIED);
        }
        verificationRequestRepository.save(verificationRequest);
        LOGGER.info("Verification request created for user: {}", verificationRequest.getUserPhoneNumber());
    }

    public boolean verifyId(String userPhoneNumber, byte[] selfieImage, byte[] idFrontImage, byte[] idBackImage) {
        LOGGER.info("Verifying user: {}", userPhoneNumber);
        CompareFacesRequest compareFacesRequest = new CompareFacesRequest()
                .withSourceImage(new Image().withBytes(ByteBuffer.wrap(selfieImage)))
                .withTargetImage(new Image().withBytes(ByteBuffer.wrap(idFrontImage)))
                .withSimilarityThreshold(70F);

        CompareFacesResult compareFacesResult = amazonRekognition.compareFaces(compareFacesRequest);
        List<CompareFacesMatch> faceMatches = compareFacesResult.getFaceMatches();

        if (!faceMatches.isEmpty()) {
            Optional<User> user = userRepository.findByUserPhoneNumber(userPhoneNumber);
            User verifiedUser = verifyPhoneNumberExist(user);
            verifiedUser.setAccountVerifiedBadge(true);
            userRepository.save(verifiedUser);
            LOGGER.info("User: {} verified", userPhoneNumber);
            return true;
        } else {
            return false;
        }
    }
}
