package com.phoenixtype.lyneapp.onboarding.service;

import com.phoenixtype.lyneapp.onboarding.model.User;
import com.phoenixtype.lyneapp.onboarding.repository.UserRepository;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioVerificationCodeService implements VerificationCodeService {

    private final UserRepository userRepository;
    private final TwilioRestClient twilioClient;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    public TwilioVerificationCodeService(UserRepository userRepository, @Value("${twilio.account.sid}") String twilioAccountSid, @Value("${twilio.auth.token}") String twilioAuthToken) {
        this.userRepository = userRepository;
        this.twilioClient = new TwilioRestClient.Builder(twilioAccountSid, twilioAuthToken).build();
    }

    @Override
    public void sendVerificationCode(String phoneNumber, String verificationCode) {
        // Find the user by phone number
        User user = userRepository.findByPhoneNumber(phoneNumber);

        // Send the verification code via SMS using the Twilio API
        Message message = Message.creator(
                new PhoneNumber(phoneNumber),
                new PhoneNumber(twilioPhoneNumber),
                "Your LYNE verification code is: " + verificationCode)
                .create();

        // Store the verification code in the user's account
        user.setVerificationCode(verificationCode);
        userRepository.save(user);
    }
}
