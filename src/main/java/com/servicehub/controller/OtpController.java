package com.servicehub.controller;

import com.servicehub.model.OtpRequest;
import com.servicehub.model.OtpResponse;
import com.servicehub.service.OtpService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private final OtpService otpService;

    public OtpController(OtpService otpService) 
    {
        this.otpService = otpService;
    }

    @PostMapping("/generate")
    public OtpResponse generateOtp(@RequestBody OtpRequest request) 
    {
        String otp = otpService.generateOtp(request.getEmail());
        // In production: send it via email or SMS here instead of returning it
        return new OtpResponse("OTP generated: " + otp, true);
    }

    @PostMapping("/validate")
    public OtpResponse validateOtp(@RequestParam String email, @RequestParam String otp) 
    {
        boolean valid = otpService.validateOtp(email, otp);
        return new OtpResponse(valid ? "OTP valid " : "OTP invalid or expired", valid);
    }
}

