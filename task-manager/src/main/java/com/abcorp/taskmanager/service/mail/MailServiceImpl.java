package com.abcorp.taskmanager.service.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{

    private final JavaMailSender mailSender;
    @Override
    public void sendOtp(String email, String otp) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("noreply@abcorp.com");
        mailMessage.setTo(email);
        mailMessage.setSubject("Password Reset OTP");
        mailMessage.setText(
                String.format(
                        "You OTP for password verification is %s",
                        otp
                )
        );
        mailSender.send(mailMessage);
    }
}
