package com.abcorp.taskmanager.service.mail;

public interface MailService {
    void sendOtp(String email, String otp);
}
