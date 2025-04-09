package com.backend.project_management.Controller;

import com.backend.project_management.Service.ProjectAdminService;
import com.backend.project_management.Service.TeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forgetPassword")
@CrossOrigin(origins = "http://localhost:3000")
public class forgetPassword1 {
    @Autowired
    private ProjectAdminService adminService;

    @Autowired
    private TeamMemberService service;

    @PostMapping("/passwordRecovery")
    public ResponseEntity<String> passwordRecovery(
            @RequestParam String role,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer otp,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) String confirmPassword) {

        switch (role.toUpperCase()) {
            case "PROJECT_ADMIN":
                return handleProjectAdminRecovery(email, otp, newPassword, confirmPassword);

            case "TEAM_MEMBER":
                return handleTeamMemberRecovery(email, otp, newPassword, confirmPassword);

            default:
                return ResponseEntity.badRequest().body("Invalid role.");
        }
    }

    private ResponseEntity<String> handleProjectAdminRecovery(String email, Integer otp, String newPassword, String confirmPassword) {
        if (email != null && otp == null && newPassword == null) {
            return ResponseEntity.ok(adminService.forgotPassword(email));
        } else if (email != null && otp != null && newPassword == null) {
            return ResponseEntity.ok(adminService.verifyOtp(email, otp));
        } else if (email != null && newPassword != null && confirmPassword != null) {
            return ResponseEntity.ok(adminService.resetPassword(email, newPassword, confirmPassword));
        } else {
            return ResponseEntity.badRequest().body("Invalid input for project admin recovery.");
        }
    }

    private ResponseEntity<String> handleTeamMemberRecovery(String email, Integer otp, String newPassword, String confirmPassword) {
        if (email != null && otp == null && newPassword == null) {
            return ResponseEntity.ok(service.forgotPassword(email));
        } else if (email != null && otp != null && newPassword == null) {
            return ResponseEntity.ok(service.verifyOtp(email, otp));
        } else if (email != null && newPassword != null && confirmPassword != null) {
            return ResponseEntity.ok(service.resetPassword(email, newPassword, confirmPassword));
        } else {
            return ResponseEntity.badRequest().body("Invalid input for team member recovery.");
        }
    }

}
