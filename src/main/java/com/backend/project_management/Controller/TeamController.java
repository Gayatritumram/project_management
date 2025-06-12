package com.backend.project_management.Controller;

import com.backend.project_management.DTO.TeamDTO;
import com.backend.project_management.Service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/teams")
@CrossOrigin(origins = "https://pjsofttech.in")
public class  TeamController {

    @Autowired
    private TeamService teamService;



    @PostMapping("/create")
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO teamDTO,
                                              @RequestParam String role,
                                              @RequestParam String email) {
        return ResponseEntity.ok(teamService.createTeam(teamDTO, role, email));
    }

    @GetMapping("getTeamById/{id}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Long id,
                                               @RequestParam String role,
                                               @RequestParam String email) {
        return ResponseEntity.ok(teamService.getTeamById(id, role, email));
    }

    @GetMapping("/getAllTeams")
    public ResponseEntity<List<TeamDTO>> getAllTeams(@RequestParam String role,
                                                     @RequestParam String email,
                                                     @RequestParam String branchCode) {
        return ResponseEntity.ok(teamService.getAllTeams(role, email, branchCode));
    }

    @PutMapping("updateTeam/{id}")
    public ResponseEntity<TeamDTO> updateTeam(@PathVariable Long id,
                                              @RequestBody TeamDTO teamDTO,
                                              @RequestParam String role,
                                              @RequestParam String email) {
        return ResponseEntity.ok(teamService.updateTeam(id, teamDTO, role, email));
    }

    @DeleteMapping("deleteTeam/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable Long id,
                                           @RequestParam String role,
                                           @RequestParam String email) {
        teamService.deleteTeam(id, role, email);
        return ResponseEntity.ok("Team Leader with ID " + id + " deleted successfully.");
    }


}