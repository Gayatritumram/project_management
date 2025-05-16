package com.backend.project_management.Controller;

import com.backend.project_management.DTO.TeamLeaderDTO;
import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Entity.TeamLeader;
import com.backend.project_management.Entity.TeamMember;
import com.backend.project_management.Service.TeamMemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/team-members")
@CrossOrigin(origins = "http://localhost:3000")
public class TeamMemberController {

    @Autowired
    private TeamMemberService service;

    @PostMapping("/create")
    public ResponseEntity<TeamMemberDTO> createTeamMember(
            @RequestParam("data") String dtoJson,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        TeamMemberDTO teamMDTO = objectMapper.readValue(dtoJson, TeamMemberDTO.class);

        TeamMember created = service.createTeamMember(teamMDTO, imageFile);

        teamMDTO.setId(created.getId());
        teamMDTO.setImageUrl(created.getImageUrl());

        return new ResponseEntity<>(teamMDTO, HttpStatus.CREATED);
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

    @PutMapping("/update-image-url/{id}")
    public ResponseEntity<TeamMemberDTO> updateImageUrl(@PathVariable Long id, @RequestBody TeamMemberDTO teamMemberDTO) {
        TeamMemberDTO updatedMember = service.updateImageUrl(id, teamMemberDTO.getImageUrl());
        return ResponseEntity.ok(updatedMember);
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