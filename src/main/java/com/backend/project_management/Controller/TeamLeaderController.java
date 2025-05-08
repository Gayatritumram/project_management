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

//    @PostMapping("/create")
//    public ResponseEntity<TeamLeaderDTO> createTeamLeader(@RequestBody TeamLeaderDTO dto) {
//        return ResponseEntity.ok(teamLeaderService.createTeamLeader(dto));
//    }


    @PostMapping("/create")
    public ResponseEntity<TeamLeaderDTO> createTeamLeader(
            @RequestParam("data") String dtoJson,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        TeamLeaderDTO teamLeaderDTO = objectMapper.readValue(dtoJson, TeamLeaderDTO.class);

        TeamLeader created = teamLeaderService.createTeamLeader(teamLeaderDTO, imageFile);

        // Update DTO with saved values (id, imageUrl, etc.)
        teamLeaderDTO.setId(created.getId());
        teamLeaderDTO.setImageUrl(created.getImageUrl());

        return new ResponseEntity<>(teamLeaderDTO, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TeamLeaderDTO>> getAll() {
        return ResponseEntity.ok(teamLeaderService.getAllTeamLeaders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamLeaderDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(teamLeaderService.getTeamLeaderById(id));
    }

    @GetMapping("/email")
    public ResponseEntity<TeamLeaderDTO> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(teamLeaderService.getTeamLeaderByEmail(email));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TeamLeaderDTO> update(@PathVariable Long id, @RequestBody TeamLeaderDTO dto) {
        return ResponseEntity.ok(teamLeaderService.updateTeamLeader(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        teamLeaderService.deleteTeamLeader(id);
        return ResponseEntity.ok("Team Leader with ID " + id + " deleted successfully.");
    }

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
