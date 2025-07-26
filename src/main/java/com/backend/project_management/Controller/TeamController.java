package com.backend.project_management.Controller;

import com.backend.project_management.DTO.TeamDTO;
import com.backend.project_management.Entity.Team;
import com.backend.project_management.Service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
@CrossOrigin(origins = "http://localhost:5173")
//@CrossOrigin(origins = "https://pjsofttech.in")
public class TeamController {

    @Autowired
    private TeamService teamService;


    @PostMapping("/createTeam")
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

    @GetMapping("/getAllTeamsWithFilters")
public ResponseEntity<Page<TeamDTO>> getAllTeams(
        @RequestParam String role,
        @RequestParam String email,
        @RequestParam(required = false) Long id,
        @RequestParam(required = false) String teamName,
        @RequestParam(required = false) String branchName,
        @RequestParam(required = false) String departmentName,
        @RequestParam(required = false) String createdByEmail,
        @RequestParam(required = false) String filterRole,
        @RequestParam(required = false) String branchCode,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String sortDir) {

    Team filter = new Team();
    filter.setId(id);
    filter.setTeamName(teamName);
    filter.setBranchName(branchName);
    filter.setDepartmentName(departmentName);
    filter.setCreatedByEmail(createdByEmail);
    filter.setRole(filterRole);
    filter.setBranchCode(branchCode);

    // Debugging output
    System.out.println("Filter object: " + filter);

    Page<TeamDTO> result = teamService.getAllTeams(
            role, email, filter, page, size, sortBy, sortDir
    );
    return ResponseEntity.ok(result);
}


}