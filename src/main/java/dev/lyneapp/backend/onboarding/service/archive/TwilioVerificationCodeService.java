package dev.lyneapp.backend.onboarding.service.archive;

import dev.lyneapp.backend.onboarding.model.User;
import dev.lyneapp.backend.onboarding.repository.UserRepository;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
@Service
@AllArgsConstructor
public class TwilioVerificationCodeService implements VerificationCodeService {

    private final UserRepository userRepository;
    private final TwilioRestClient twilioClient;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;


    @Override
    public void sendVerificationCode(String phoneNumber, String verificationCode) {
        // Find the user by phone number
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        User verifiedUser = UserOnboardingService.verifyPhoneNumberExist(user);

        // Send the verification code via SMS using the Twilio API
        Message message = Message.creator(
                new PhoneNumber(phoneNumber),
                new PhoneNumber(twilioPhoneNumber),
                "Your LYNE verification code is: " + verificationCode)
                .create();

        // Store the verification code in the user's account
        verifiedUser.setVerificationCode(verificationCode);
        userRepository.save(verifiedUser);
    }
}

*/
