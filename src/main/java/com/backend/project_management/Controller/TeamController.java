package com.backend.project_management.Controller;

import com.backend.project_management.DTO.TeamDTO;
import com.backend.project_management.Service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/teams")
@CrossOrigin(origins = "http://localhost:3000")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/create")
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO teamDTO) {
        return ResponseEntity.ok(teamService.createTeam(teamDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    @GetMapping
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamDTO> updateTeam(@PathVariable Long id, @RequestBody TeamDTO teamDTO) {
        return ResponseEntity.ok(teamService.updateTeam(id, teamDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}