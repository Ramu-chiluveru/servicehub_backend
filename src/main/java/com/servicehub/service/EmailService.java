package com.servicehub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            // Print OTP to console (for debugging/testing)
            System.out.println("Generated OTP for " + toEmail + ": " + otp);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Your OTP Code - ServiceHub");
            message.setText(buildOtpEmailBody(otp));

            mailSender.send(message);
            System.out.println("OTP email sent successfully to: " + toEmail);

        } catch (MailException e) {
            System.err.println("Failed to send OTP email to " + toEmail + ": " + e.getMessage());
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage(), e);
        }
    }

    private String buildOtpEmailBody(String otp) {
        return String.format(
                "Dear User,\n\n" +
                        "Your One-Time Password (OTP) is: %s\n\n" +
                        "This OTP is valid for 5 minutes. Please do not share this code with anyone.\n\n" +
                        "If you didn't request this OTP, please ignore this email.\n\n" +
                        "Best regards,\n" +
                        "ServiceHub Team", otp);
    }
}