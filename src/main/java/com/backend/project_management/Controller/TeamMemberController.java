package com.backend.project_management.Controller;

import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Service.TeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/team-members")
public class TeamMemberController {
    @Autowired
    private TeamMemberService service;

    public TeamMemberController(TeamMemberService service) {
        this.service = service;
    }

    @PostMapping("/createTeamMember")
    public TeamMemberDTO create(@RequestBody TeamMemberDTO dto) {
        return service.createTeamMember(dto);
    }

    @GetMapping("/getByIdTeamMember/{id}")
    public TeamMemberDTO getById(@PathVariable Long id) {
        return service.getTeamMemberById(id);
    }

    @GetMapping
    public List<TeamMemberDTO> getAll() {
        return service.getAllTeamMembers();
    }

    @PutMapping("updateTeamMember/{id}")
    public TeamMemberDTO update(@PathVariable Long id, @RequestBody TeamMemberDTO dto) {
        return service.updateTeamMember(id, dto);
    }

    @DeleteMapping("deleteTeamMember/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteTeamMember(id);
    }
}

