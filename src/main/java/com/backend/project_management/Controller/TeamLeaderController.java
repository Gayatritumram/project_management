package com.backend.project_management.Controller;

import com.backend.project_management.DTO.TeamLeaderDTO;
import com.backend.project_management.Service.TeamLeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team-leader")
public class TeamLeaderController {

    @Autowired
    private TeamLeaderService teamLeaderService;

    // Create Team Leader
    @PostMapping("/create")
    public ResponseEntity<TeamLeaderDTO> createTeamLeader(@RequestBody TeamLeaderDTO teamLeaderDTO) {
        return ResponseEntity.ok(teamLeaderService.createTeamLeader(teamLeaderDTO));
    }

    // Get all Team Leaders
    @GetMapping("/all")
    public ResponseEntity<List<TeamLeaderDTO>> getAllTeamLeaders() {
        return ResponseEntity.ok(teamLeaderService.getAllTeamLeaders());
    }

    // Get Team Leader by ID
    @GetMapping("/{id}")
    public ResponseEntity<TeamLeaderDTO> getTeamLeaderById(@PathVariable Long id) {
        return ResponseEntity.ok(teamLeaderService.getTeamLeaderById(id));
    }

    // Get Team Leader by Email
    @GetMapping("/email")
    public ResponseEntity<TeamLeaderDTO> getTeamLeaderByEmail(@RequestParam String email) {
        return ResponseEntity.ok(teamLeaderService.getTeamLeaderByEmail(email));
    }

    // Update Team Leader
    @PutMapping("/update/{id}")
    public ResponseEntity<TeamLeaderDTO> updateTeamLeader(@PathVariable Long id, @RequestBody TeamLeaderDTO teamLeaderDTO) {
        return ResponseEntity.ok(teamLeaderService.updateTeamLeader(id, teamLeaderDTO));
    }

    // Delete Team Leader
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTeamLeader(@PathVariable Long id) {
        teamLeaderService.deleteTeamLeader(id);
        return ResponseEntity.ok("Team Leader with ID " + id + " deleted successfully.");
    }
}
