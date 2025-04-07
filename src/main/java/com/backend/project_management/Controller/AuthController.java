package com.backend.project_management.Controller;

import com.backend.project_management.DTO.ProjectAdminDTO;
import com.backend.project_management.Exception.RequestNotFound;
import com.backend.project_management.Model.JwtRequest;
import com.backend.project_management.Model.JwtResponse;
import com.backend.project_management.Repository.TeamMemberRepository;
import com.backend.project_management.Service.ProjectAdminService;

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

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private ProjectAdminService adminService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    /**
     * Register a new Admin
     */
    @PostMapping("/register")
    public ResponseEntity<ProjectAdminDTO> register(@RequestBody ProjectAdminDTO user) {
        return new ResponseEntity<>(adminService.registerAdmin(user), HttpStatus.CREATED);
    }

    /**
     * Unified Login Endpoint for Admins and Team Members
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest request) {
        try {
            this.authenticate(request.getEmail(), request.getPassword());

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

            // Determine user role dynamically
            UserRole assignedRole = determineUserRole(request.getEmail());

            String token = this.helper.generateToken(userDetails, assignedRole);

            JwtResponse response = JwtResponse.builder()
                    .jwtToken(token)
                    .username(userDetails.getUsername())
                    .role(assignedRole.name())
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        } catch (RequestNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Authenticate user credentials
     */
    private void authenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(email, password);
        manager.authenticate(authentication);
    }

    /**
     * Determine User Role dynamically based on TeamMember data
     */
    private UserRole determineUserRole(String email) {
        return teamMemberRepository.findByEmail(email)
                .map(teamMember -> teamMember.isLeader() ? UserRole.TEAM_LEADER : UserRole.TEAM_MEMBER)
                .orElse(UserRole.ADMIN);
    }
}
//Authentication
