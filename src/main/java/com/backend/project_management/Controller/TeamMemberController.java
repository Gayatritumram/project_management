package com.backend.project_management.Controller;

import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Service.TeamMemberService;
import com.backend.project_management.Util.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/team-members")
@CrossOrigin(origins = "http://localhost:3000")
public class TeamMemberController {
    @Autowired
    private TeamMemberService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private AuthenticationManager manager;




    @PostMapping("/createTeamMember")
    public TeamMemberDTO create(@RequestBody TeamMemberDTO dto) {
        return service.createTeamMember(dto);
    }


    @GetMapping("/getByIdTeamMember/{id}")
    public TeamMemberDTO getById(@PathVariable Long id) {
        return service.getTeamMemberById(id);
    }

    @GetMapping("/getAllTeamMember")
    public ResponseEntity<List<TeamMemberDTO>> getAllTeamMember() {
        return ResponseEntity.ok(service.getAllNonLeaderTeamMembers());
    }

    @GetMapping("/getAllTeamLeader")
    public ResponseEntity<List<TeamMemberDTO>> getAllTeamLeader() {
        return ResponseEntity.ok(service.getAllLeaderTeamMembers());
    }

    @PutMapping("/make-leader/{id}")
    public ResponseEntity<String> makeTeamLeader(@PathVariable Long id) {
        service.makeTeamLeader(id);
        return ResponseEntity.ok("Team member with ID " + id + " is now a Team Leader.");
    }



    @PutMapping("updateTeamMember/{id}")
    public TeamMemberDTO update(@PathVariable Long id, @RequestBody TeamMemberDTO dto) {
        return service.updateTeamMember(id, dto);
    }

    @DeleteMapping("deleteTeamMember/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteTeamMember(id);
        return ResponseEntity.ok("Team Member deleted Successfully  ");
    }


    // Forgot Password
    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        String message = service.forgotPassword(email);
        return ResponseEntity.ok(message);
    }

    // Verify OTP
    @PostMapping("/verifyOtp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam int otp) {
        String message = service.verifyOtp(email, otp);
        return ResponseEntity.ok(message);
    }

    // Reset Password
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String newPassword, @RequestParam String confirmPassword) {
        String message = service.resetPassword(email, newPassword, confirmPassword);
        return ResponseEntity.ok(message);
    }



}

