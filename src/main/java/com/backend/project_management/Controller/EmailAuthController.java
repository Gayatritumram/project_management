package com.backend.project_management.Controller;

import com.backend.project_management.Service.EmailAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailAuthController {

    @Autowired
    private EmailAuthService authService;

    @GetMapping("/forgot-password")
    public String forgotPassword(@RequestParam String username) {
        // Here, you would fetch the user by username and get their email address
        // For simplicity, let's assume the email is fetched correctly
        String email = "user@example.com"; // Replace with actual user email
        String token = authService.generatePasswordResetToken(username);
        authService.sendPasswordResetEmail(email, token);
        return "Password reset email sent!";
    }
}
