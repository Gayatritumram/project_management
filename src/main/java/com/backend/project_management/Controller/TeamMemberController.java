package com.backend.project_management.Controller;

import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Service.TeamMemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/team-members")
public class TeamMemberController {
    private final TeamMemberService service;

    public TeamMemberController(TeamMemberService service) {
        this.service = service;
    }

    @PostMapping
    public TeamMemberDTO create(@RequestBody TeamMemberDTO dto) {
        return service.createTeamMember(dto);
    }

    @GetMapping("/{id}")
    public TeamMemberDTO getById(@PathVariable Long id) {
        return service.getTeamMemberById(id);
    }

    @GetMapping
    public List<TeamMemberDTO> getAll() {
        return service.getAllTeamMembers();
    }

    @PutMapping("/{id}")
    public TeamMemberDTO update(@PathVariable Long id, @RequestBody TeamMemberDTO dto) {
        return service.updateTeamMember(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteTeamMember(id);
    }
}

