package com.backend.project_management.Controller;

import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Entity.TeamMember;
import com.backend.project_management.Exception.RequestNotFound;
import com.backend.project_management.Model.JwtRequest;
import com.backend.project_management.Model.JwtResponse;
import com.backend.project_management.Repository.TeamMemberRepository;
import com.backend.project_management.Service.TeamMemberService;
import com.backend.project_management.UserPermission.UserRole;
import com.backend.project_management.Util.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/team-members")
@CrossOrigin(origins = "http://localhost:3000")
public class TeamMemberController {
    @Autowired
    private TeamMemberService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private AuthenticationManager manager;




    @PostMapping("/createTeamMember")
    public TeamMemberDTO create(@RequestBody TeamMemberDTO dto) {
        return service.createTeamMember(dto);
    }


    @GetMapping("/getByIdTeamMember/{id}")
    public TeamMemberDTO getById(@PathVariable Long id) {
        return service.getTeamMemberById(id);
    }

    @GetMapping("/getAllTeamMember")
    public ResponseEntity<List<TeamMemberDTO>> getAllTeamMember() {
        return ResponseEntity.ok(service.getAllNonLeaderTeamMembers());
    }

    @GetMapping("/getAllTeamLeader")
    public ResponseEntity<List<TeamMemberDTO>> getAllTeamLeader() {
        return ResponseEntity.ok(service.getAllLeaderTeamMembers());
    }

    @PutMapping("/make-leader/{id}")
    public ResponseEntity<String> makeTeamLeader(@PathVariable Long id) {
        service.makeTeamLeader(id);
        return ResponseEntity.ok("Team member with ID " + id + " is now a Team Leader.");
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

