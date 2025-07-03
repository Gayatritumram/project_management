package com.backend.project_management.Controller;

import com.backend.project_management.DTO.TeamLeaderDTO;
import com.backend.project_management.Entity.TeamLeader;
import com.backend.project_management.Service.TeamLeaderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/team-leader")
@CrossOrigin(origins = "https://pjsofttech.in")
public class TeamLeaderController {

    @Autowired
    private TeamLeaderService teamLeaderService;


    @PostMapping("/createTeamLeader")
    public ResponseEntity<TeamLeaderDTO> createTeamLeader(@RequestParam("data") String dtoJson,
                                                          @RequestParam(value = "image", required = false) MultipartFile imageFile,
                                                          @RequestParam String role,
                                                          @RequestParam String email
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        TeamLeaderDTO teamLeaderDTO = objectMapper.readValue(dtoJson, TeamLeaderDTO.class);

        TeamLeader created = teamLeaderService.createTeamLeader(teamLeaderDTO, imageFile, role, email);
        
        // Map the saved entity back to DTO to include all fields
        TeamLeaderDTO responseDTO = new TeamLeaderDTO();
        responseDTO.setId(created.getId());
        responseDTO.setName(created.getName());
        responseDTO.setEmail(created.getEmail());
        responseDTO.setPhone(created.getPhone());
        responseDTO.setAddress(created.getAddress());
        responseDTO.setDepartmentName(created.getDepartmentName());
        responseDTO.setBranchName(created.getBranchName());
        responseDTO.setJoinDate(created.getJoinDate());
        responseDTO.setImageUrl(created.getImageUrl());
        responseDTO.setCreatedByEmail(created.getCreatedByEmail());
        responseDTO.setRole(created.getRole());
        responseDTO.setBranchCode(created.getBranchCode());
        if (created.getTeam() != null) {
            responseDTO.setTeamId(created.getTeam().getId());
        }

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/getAllTeamLeaders")
    public ResponseEntity<List<TeamLeaderDTO>> getAllTeamLeaders(@RequestParam String role,
                                                      @RequestParam String email,
                                                      @RequestParam String branchCode) {
        return ResponseEntity.ok(teamLeaderService.getAllTeamLeaders(role, email, branchCode));
    }

    @GetMapping("getTeamLeadersById/{id}")
    public ResponseEntity<TeamLeaderDTO> getTeamLeadersById(@PathVariable Long id,
                                                 @RequestParam String role,
                                                 @RequestParam String email) {
        return ResponseEntity.ok(teamLeaderService.getTeamLeaderById(id, role, email));
    }

    @GetMapping("/getTeamLeadersByEmail/{emailFind}")
    public ResponseEntity<TeamLeaderDTO> getTeamLeadersByEmail(@RequestParam String email,
                                                    @RequestParam String role,
                                                    @PathVariable String emailFind) {
        return ResponseEntity.ok(teamLeaderService.getTeamLeaderByEmail(email,role,emailFind));
    }

    @PutMapping("/updateTeamLeaders/{id}")
    public ResponseEntity<TeamLeaderDTO> updateTeamLeaders(@PathVariable Long id, @RequestBody TeamLeaderDTO dto,
                                                @RequestParam String role,
                                                @RequestParam String email) {
        return ResponseEntity.ok(teamLeaderService.updateTeamLeader(id, dto, role, email));
    }

    @DeleteMapping("/deleteTeamLeader/{id}")
    public ResponseEntity<String> deleteTeamLeader(@PathVariable Long id,
                                         @RequestParam String role,
                                         @RequestParam String email) {
        teamLeaderService.deleteTeamLeader(id, role, email);
        return ResponseEntity.ok("Team Leader with ID " + id + " deleted successfully.");
    }


    @PutMapping("/updateTeamLeaderProfilePicture/{leaderId}")
    public ResponseEntity<String> updateTeamLeaderProfilePicture(@PathVariable Long leaderId,
                                                                 @RequestParam(value = "image", required = false) MultipartFile imageFile,
                                                                 @RequestParam String role,
                                                                 @RequestParam String email){

        teamLeaderService.updateTeamMemberProfilePicture(leaderId,imageFile, role, email);
        return ResponseEntity.ok("Profile picture updated successfully.");

    }

    @GetMapping("/getTeamLeaderByName")
    public ResponseEntity<TeamLeader> getTeamLeaderByName(
            @RequestParam String name,
            @RequestParam String role,
            @RequestParam String email
    ) {
        TeamLeader leader = teamLeaderService.getTeamLeaderByName(name, role, email);
        return ResponseEntity.ok(leader);
    }

    @GetMapping("/getAllTeamLeadersWithFilters")
    public ResponseEntity<Page<TeamLeaderDTO>> getAllTeamLeaders(
            @RequestParam String role,
            @RequestParam String email,
            @RequestParam String branchCode,
            @RequestParam(required = false) String searchBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Page<TeamLeaderDTO> result = teamLeaderService.getAllTeamLeaders(
                role, email, branchCode, searchBy, page, size, sortBy, sortDir
        );
        return ResponseEntity.ok(result);
    }





}
