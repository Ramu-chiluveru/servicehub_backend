//package com.servicehub.service;
//
//import com.servicehub.controller.ProviderController;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Service;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.security.SecureRandom;
//import java.util.concurrent.TimeUnit;
//
//@Service
//public class OtpService {
//
//    @Value("${otp.expiry.minutes:5}")
//    private long otpExpiryMinutes;
//
//    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);
//
//    @Value("${otp.length:6}")
//    private int otpLength;
//
//    private static final String OTP_KEY_PREFIX = "OTP:";
//
//    @Autowired
//    private StringRedisTemplate redisTemplate;
//
//    public String generateOtp(String email) {
//        String otp = generateRandomOtp();
//        logger.info("step2.1 otp gen working fine with otp: "+otp);
//        String key = OTP_KEY_PREFIX + email;
//
//        // Store OTP in Redis with TTL
//        try{
//            redisTemplate.opsForValue().set(key, otp, otpExpiryMinutes, TimeUnit.MINUTES);
//            logger.info(("step2.2 reddis working fine"));
//            return otp;
//        }
//        catch (Exception e)
//        {
//            return  e.getMessage();
//        }
//
//    }
//
//    public boolean validateOtp(String email, String userOtp) {
//        String key = OTP_KEY_PREFIX + email;
//        String storedOtp = redisTemplate.opsForValue().get(key);
//
//        if (storedOtp != null && storedOtp.equals(userOtp)) {
//            redisTemplate.delete(key); // one-time use
//            return true;
//        }
//        return false;
//    }
//
//    private String generateRandomOtp() {
//        SecureRandom random = new SecureRandom();
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < otpLength; i++) {
//            sb.append(random.nextInt(10));
//        }
//        return sb.toString();
//    }
//}

package com.servicehub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    @Value("${otp.expiry.minutes:5}")
    private long otpExpiryMinutes;

    @Value("${otp.length:6}")
    private int otpLength;

    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);
    private static final String OTP_KEY_PREFIX = "OTP:";

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * Test Redis connection - call this method manually if needed
     */
    public void testRedisConnectionOnStartup() {
        logger.info("Testing Redis connection...");
        boolean isConnected = testRedisConnection();
        if (isConnected) {
            logger.info("✅ Redis connection established successfully");
        } else {
            logger.error("❌ Redis connection failed");
        }
    }

    /**
     * Generate and store OTP for the given email
     */
    public String generateOtp(String email) {
        try {
            String otp = generateRandomOtp();
            logger.info("Generated OTP for {}: {}", email, otp);

            String key = OTP_KEY_PREFIX + email;

            // Store OTP in Redis with TTL
            redisTemplate.opsForValue().set(key, otp, otpExpiryMinutes, TimeUnit.MINUTES);
            logger.info("OTP stored successfully in Redis for email: {} with {} minutes expiry",
                    email, otpExpiryMinutes);

            return otp;

        } catch (Exception e) {
            logger.error("Failed to generate/store OTP for email: {}. Error: {}", email, e.getMessage());
            throw new RuntimeException("Failed to generate OTP. Please try again.", e);
        }
    }

    /**
     * Validate OTP for the given email
     */
    public boolean validateOtp(String email, String userOtp) {
        try {
            String key = OTP_KEY_PREFIX + email;
            String storedOtp = redisTemplate.opsForValue().get(key);

            if (storedOtp == null) {
                logger.warn("No OTP found for email: {} (might be expired)", email);
                return false;
            }

            if (storedOtp.equals(userOtp)) {
                // Delete OTP after successful validation (one-time use)
                redisTemplate.delete(key);
                logger.info("OTP validation successful for email: {}", email);
                return true;
            }

            logger.warn("OTP validation failed for email: {} (incorrect OTP)", email);
            return false;

        } catch (Exception e) {
            logger.error("Redis error during OTP validation for email: {}. Error: {}", email, e.getMessage());
            return false;
        }
    }

    /**
     * Check if OTP exists for the given email (not expired yet)
     */
    public boolean hasOtp(String email) {
        try {
            String key = OTP_KEY_PREFIX + email;
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            logger.error("Error checking OTP existence for email: {}", email, e);
            return false;
        }
    }

    /**
     * Get remaining TTL for OTP in seconds
     */
    public Long getOtpTtl(String email) {
        try {
            String key = OTP_KEY_PREFIX + email;
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("Error getting OTP TTL for email: {}", email, e);
            return null;
        }
    }

    /**
     * Test Redis connection
     */
    public boolean testRedisConnection() {
        try {
            String result = redisTemplate.getConnectionFactory().getConnection().ping();
            logger.info("Redis connection test: SUCCESS - {}", result);
            return true;
        } catch (Exception e) {
            logger.error("Redis connection test: FAILED - {}", e.getMessage());
            return false;
        }
    }

    /**
     * Generate random numeric OTP
     */
    private String generateRandomOtp() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(otpLength);

        for (int i = 0; i < otpLength; i++) {
            sb.append(random.nextInt(10)); // Append random digit (0-9)
        }

        return sb.toString();
    }

    /**
     * Resend OTP - generates new OTP and replaces existing one
     */
    public String resendOtp(String email) {
        try {
            // First, delete any existing OTP
            String key = OTP_KEY_PREFIX + email;
            redisTemplate.delete(key);

            // Generate and store new OTP
            return generateOtp(email);

        } catch (Exception e) {
            logger.error("Failed to resend OTP for email: {}", email, e);
            throw new RuntimeException("Failed to resend OTP. Please try again.", e);
        }
    }

    /**
     * Clear OTP for the given email
     */
    public boolean clearOtp(String email) {
        try {
            String key = OTP_KEY_PREFIX + email;
            Boolean deleted = redisTemplate.delete(key);
            if (Boolean.TRUE.equals(deleted)) {
                logger.info("OTP cleared for email: {}", email);
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("Error clearing OTP for email: {}", email, e);
            return false;
        }
    }
}