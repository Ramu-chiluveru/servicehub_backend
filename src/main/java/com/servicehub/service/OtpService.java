package com.servicehub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    private static final long OTP_EXPIRY_MINUTES = 10;
    private static final int OTP_LENGTH = 6;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public String generateOtp(String email) {
        String otp = generateRandomOtp();
        String key = "OTP:" + email;

        // Store OTP in Redis with 10-minute TTL
        redisTemplate.opsForValue().set(key, otp, OTP_EXPIRY_MINUTES, TimeUnit.MINUTES);
        return otp;
    }

    public boolean validateOtp(String email, String userOtp) {
        String key = "OTP:" + email;
        String storedOtp = redisTemplate.opsForValue().get(key);

        if (storedOtp != null && storedOtp.equals(userOtp)) 
        {
            redisTemplate.delete(key); // OTP is one-time use
            return true;
        }
        return false;
    }

    private String generateRandomOtp() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) 
        {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
