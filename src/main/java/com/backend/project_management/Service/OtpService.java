package com.backend.project_management.Service;


public interface OtpService {
    boolean generateAndSendOTP(String email);
    boolean verifyOTP(String email, int otp);
}

