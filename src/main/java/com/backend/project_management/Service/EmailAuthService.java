package com.backend.project_management.Service;

public interface EmailAuthService {

    String generatePasswordResetToken(String userId);
    void sendPasswordResetEmail(String email, String token);

}
