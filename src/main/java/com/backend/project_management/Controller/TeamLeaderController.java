package com.backend.project_management.Controller;

import com.backend.project_management.DTO.TeamLeaderDTO;
import com.backend.project_management.Entity.TeamLeader;
import com.backend.project_management.Service.TeamLeaderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/team-leader")
public class TeamLeaderController {

    @Autowired
    private TeamLeaderService teamLeaderService;


    @PostMapping("/create")
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

        // Update DTO with saved values (id, imageUrl, etc.)
        teamLeaderDTO.setId(created.getId());
        teamLeaderDTO.setImageUrl(created.getImageUrl());

        return new ResponseEntity<>(teamLeaderDTO, HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<TeamLeaderDTO>> getAll(@RequestParam String role,
                                                      @RequestParam String email,
                                                      @RequestParam String branchCode) {
        return ResponseEntity.ok(teamLeaderService.getAllTeamLeaders(role, email, branchCode));
    }

    @GetMapping("getById/{id}")
    public ResponseEntity<TeamLeaderDTO> getById(@PathVariable Long id,
                                                 @RequestParam String role,
                                                 @RequestParam String email) {
        return ResponseEntity.ok(teamLeaderService.getTeamLeaderById(id, role, email));
    }

    @GetMapping("/email/{emailFind}")
    public ResponseEntity<TeamLeaderDTO> getByEmail(@RequestParam String email,
                                                    @RequestParam String role,
                                                    @PathVariable String emailFind) {
        return ResponseEntity.ok(teamLeaderService.getTeamLeaderByEmail(email,role,emailFind));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TeamLeaderDTO> update(@PathVariable Long id, @RequestBody TeamLeaderDTO dto,
                                                @RequestParam String role,
                                                @RequestParam String email) {
        return ResponseEntity.ok(teamLeaderService.updateTeamLeader(id, dto, role, email));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id,
                                         @RequestParam String role,
                                         @RequestParam String email) {
        teamLeaderService.deleteTeamLeader(id, role, email);
        return ResponseEntity.ok("Team Leader with ID " + id + " deleted successfully.");
    }

    //updateImage API for team leader









    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        return ResponseEntity.ok(teamLeaderService.forgotPassword(email));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam int otp) {
        return ResponseEntity.ok(teamLeaderService.verifyOtp(email, otp));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String email,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword
    ) {
        return ResponseEntity.ok(teamLeaderService.resetPassword(email, newPassword, confirmPassword));
    }
}
