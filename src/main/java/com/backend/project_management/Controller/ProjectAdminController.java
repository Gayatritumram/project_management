package com.backend.project_management.Controller;
import com.backend.project_management.Service.ProjectAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class ProjectAdminController {
    @Autowired
    private ProjectAdminService adminService;



    // Forgot Password
    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        String message = adminService.forgotPassword(email);
        return ResponseEntity.ok(message);
    }

    // Verify OTP
    @PostMapping("/verifyOtp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam int otp) {
        String message = adminService.verifyOtp(email, otp);
        return ResponseEntity.ok(message);
    }

    // Reset Password
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String newPassword, @RequestParam String confirmPassword) {
        String message = adminService.resetPassword(email, newPassword, confirmPassword);
        return ResponseEntity.ok(message);
    }








}
