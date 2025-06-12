package com.backend.project_management.Controller;

import com.backend.project_management.DTO.TeamLeaderDTO;
import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Entity.TeamLeader;
import com.backend.project_management.Entity.TeamMember;
import com.backend.project_management.Model.JwtRequest;
import com.backend.project_management.Model.JwtResponse;
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
@CrossOrigin(origins = "https://pjsofttech.in")
public class TeamMemberController {

    @Autowired
    private TeamMemberService service;

    @PostMapping("/create")
    public ResponseEntity<TeamMemberDTO> createTeamMember(@RequestParam("data") String dtoJson,
                                                          @RequestParam(value = "image", required = false) MultipartFile imageFile,
                                                          @RequestParam String role,
                                                          @RequestParam String email
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        TeamMemberDTO teamMDTO = objectMapper.readValue(dtoJson, TeamMemberDTO.class);

        TeamMember created = service.createTeamMember(teamMDTO, imageFile, role, email);

        teamMDTO.setId(created.getId());
        teamMDTO.setImageUrl(created.getImageUrl());

        return new ResponseEntity<>(teamMDTO, HttpStatus.CREATED);
    }


    // Get Team Member by ID
    @GetMapping("getById/{id}")
    public ResponseEntity<TeamMemberDTO> getById(@PathVariable Long id,
                                                 @RequestParam String role,
                                                 @RequestParam String email) {
        return ResponseEntity.ok(service.getTeamMemberById(id, role, email));
    }

    // Get all non-leader Team Members
    @GetMapping("/getAllTeamMembers")
    public ResponseEntity<List<TeamMemberDTO>> getAllTeamMembers(@RequestParam String role,
                                                                 @RequestParam String email,
                                                                 @RequestParam String branchCode) {
        return ResponseEntity.ok(service.getAllTeamMembers(role, email, branchCode));
    }

    // Promote a Team Member to Team Leader
    @PutMapping("/promoteToLeader/{id}")
    public ResponseEntity<String> promoteToLeader(@PathVariable Long id,
                                                  @RequestParam String role,
                                                  @RequestParam String email) {
        service.makeTeamLeader(id, role, email);
        return ResponseEntity.ok("Team member with ID " + id + " is now a Team Leader.");
    }

    // Update Team Member
    @PutMapping("/update/{id}")
    public ResponseEntity<TeamMemberDTO> update(@PathVariable Long id,
                                                @RequestBody TeamMemberDTO dto,
                                                @RequestParam String role,
                                                @RequestParam String email) {
        return ResponseEntity.ok(service.updateTeamMember(id, dto, role, email));
    }

    // Delete Team Member
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id,
                                         @RequestParam String role,
                                         @RequestParam String email) {
        service.deleteTeamMember(id, role, email);
        return ResponseEntity.ok("Team Member deleted successfully.");
    }






    // Forgot Password - Send OTP





}