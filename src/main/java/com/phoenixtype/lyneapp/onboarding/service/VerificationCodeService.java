package com.phoenixtype.lyneapp.onboarding.service;

public interface VerificationCodeService {
    void sendVerificationCode(String phoneNumber, String verificationCode) throws Exception;
}
