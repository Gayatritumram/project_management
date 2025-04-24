package com.backend.project_management.Controller;

import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Service.TeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team-members")
@CrossOrigin(origins = "http://localhost:3000")
public class TeamMemberController {

    @Autowired
    private TeamMemberService service;

    // Create new Team Member
    @PostMapping("/create")
    public ResponseEntity<TeamMemberDTO> create(@RequestBody TeamMemberDTO dto) {
        return ResponseEntity.ok(service.createTeamMember(dto));
    }

    // Get Team Member by ID
    @GetMapping("/{id}")
    public ResponseEntity<TeamMemberDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getTeamMemberById(id));
    }

    // Get all non-leader Team Members
    @GetMapping("/members")
    public ResponseEntity<List<TeamMemberDTO>> getAllTeamMembers() {
        return ResponseEntity.ok(service.getAllTeamMembers());
    }

    // Promote a Team Member to Team Leader
    @PutMapping("/promote/{id}")
    public ResponseEntity<String> promoteToLeader(@PathVariable Long id) {
        service.makeTeamLeader(id);
        return ResponseEntity.ok("Team member with ID " + id + " is now a Team Leader.");
    }

    // Update Team Member
    @PutMapping("/update/{id}")
    public ResponseEntity<TeamMemberDTO> update(@PathVariable Long id, @RequestBody TeamMemberDTO dto) {
        return ResponseEntity.ok(service.updateTeamMember(id, dto));
    }

    // Delete Team Member
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteTeamMember(id);
        return ResponseEntity.ok("Team Member deleted successfully.");
    }

    // Forgot Password - Send OTP
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        String response = service.forgotPassword(email);
        return ResponseEntity.ok(response);
    }

    // Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam int otp) {
        String response = service.verifyOtp(email, otp);
        return ResponseEntity.ok(response);
    }

    // Reset Password
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String email,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword
    ) {
        String response = service.resetPassword(email, newPassword, confirmPassword);
        return ResponseEntity.ok(response);
    }
}