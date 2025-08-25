//package com.servicehub.controller;
//
//import com.servicehub.model.OtpRequest;
//import com.servicehub.model.OtpResponse;
//import com.servicehub.service.OtpService;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/otp")
//public class OtpController {
//
//    private final OtpService otpService;
//
//    public OtpController(OtpService otpService)
//    {
//        this.otpService = otpService;
//    }
//
//    @PostMapping("/generate")
//    public OtpResponse generateOtp(@RequestBody OtpRequest request)
//    {
//        String otp = otpService.generateOtp(request.getEmail());
//        // In production: send it via email or SMS here instead of returning it
//        return new OtpResponse("OTP generated: " + otp, true);
//    }
//
//    @PostMapping("/validate")
//    public OtpResponse validateOtp(@RequestParam String email, @RequestParam String otp)
//    {
//        boolean valid = otpService.validateOtp(email, otp);
//        return new OtpResponse(valid ? "OTP valid " : "OTP invalid or expired", valid);
//    }
//}
//

// EmailService.java

package com.servicehub.controller;

import com.servicehub.model.OtpRequest;
import com.servicehub.model.OtpResponse;
import com.servicehub.service.OtpService;
import com.servicehub.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/otp")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class OtpController {

    private static final Logger logger = LoggerFactory.getLogger(OtpController.class);

    private final OtpService otpService;
    private final EmailService emailService;

    public OtpController(OtpService otpService, EmailService emailService) {
        this.otpService = otpService;
        this.emailService = emailService;
    }

    @PostMapping("/generate")
    public ResponseEntity<OtpResponse> generateOtp(@RequestBody OtpRequest request) {
        logger.info("OTP request: "+request.getEmail());
        try {
            String email = request.getEmail();
            logger.info("step1 working fine");
            String otp = otpService.generateOtp(email);
            logger.info(("step2 working fine"));
            emailService.sendOtpEmail(email, otp);
            logger.info(("step3 working fine"));
            return ResponseEntity.ok(
                    new OtpResponse("OTP sent successfully to " + email, true)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new OtpResponse("Failed to send OTP: " + e.getMessage(), false));
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<OtpResponse> validateOtp(@RequestBody OtpRequest request) {
        boolean valid = otpService.validateOtp(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(
                new OtpResponse(valid ? "OTP validated successfully" : "OTP invalid or expired", valid)
        );
    }
}
