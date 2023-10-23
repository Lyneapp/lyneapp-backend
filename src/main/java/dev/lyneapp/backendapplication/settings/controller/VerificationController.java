package dev.lyneapp.backendapplication.settings.controller;

import dev.lyneapp.backendapplication.settings.model.VerificationDTO;
import dev.lyneapp.backendapplication.settings.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/settings/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final static Logger LOGGER = LoggerFactory.getLogger(VerificationController.class);
    private final VerificationService verificationService;

    @PostMapping("/request")
    public ResponseEntity<?> createVerificationRequest(@RequestBody VerificationDTO verificationRequest) {
        LOGGER.info("Verification request received for user with id: {}", verificationRequest.getUserPhoneNumber());
        verificationService.createVerificationRequest(verificationRequest);
        return ResponseEntity.ok("User is verified");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyId(@RequestParam("userPhoneNumber") String userPhoneNumber,
                                      @RequestParam("selfieImage") MultipartFile selfieImage,
                                      @RequestParam("idFrontImage") MultipartFile idFrontImage,
                                      @RequestParam("idBackImage") MultipartFile idBackImage) {
        LOGGER.info("Verification request received for user with id: {}", userPhoneNumber);

        try {
            byte[] selfieImageBytes = selfieImage.getBytes();
            byte[] idFrontImageBytes = idFrontImage.getBytes();
            byte[] idBackImageBytes = idBackImage.getBytes();

            boolean result = verificationService.verifyId(userPhoneNumber, selfieImageBytes, idFrontImageBytes, idBackImageBytes);

            if (result) {
                LOGGER.info("ID verification successful for user with id: {}", userPhoneNumber);
                return ResponseEntity.ok("ID verification successful.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("ID verification failed. Images do not match.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during ID verification.");
        }
    }
}
