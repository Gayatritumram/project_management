package com.backend.project_management.Controller;

import com.backend.project_management.DTO.ProjectAdminDTO;
import com.backend.project_management.Exception.RequestNotFound;
import com.backend.project_management.Exception.EmailAlreadyExistsException;
import com.backend.project_management.Model.JwtRequest;
import com.backend.project_management.Model.JwtResponse;
import com.backend.project_management.Repository.ProjectAdminRepo;
import com.backend.project_management.Repository.TeamLeaderRepository;
import com.backend.project_management.Repository.TeamMemberRepository;
import com.backend.project_management.Service.ProjectAdminService;

import com.backend.project_management.UserPermission.UserRole;
import com.backend.project_management.Util.JwtHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import com.backend.project_management.Exception.DuplicateEmailHandler;


@CrossOrigin(origins = "http://localhost:3000")
//@CrossOrigin(origins = "https://pjsofttech.in")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private ProjectAdminService adminService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TeamMemberRepository teamMemberRepository;
    @Autowired
    private TeamLeaderRepository teamLeaderRepository;
    @Autowired
    private ProjectAdminRepo projectAdminRepo;

    @Autowired
    private DuplicateEmailHandler duplicateEmailHandler;

    /**
     * Register a new Admin
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody ProjectAdminDTO user) {
        try {
            // Check if email already exists using the handler
            if (duplicateEmailHandler.isEmailAlreadyRegistered(user.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email is already registered, please use a different email");
            }
            
            // Validate name format
            if (user.getName() != null && !user.getName().matches("^[a-zA-Z ]+$")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Numbers are not allowed in name. Please use only letters and spaces.");
            }
            
            // Proceed with registration if email is unique
            ProjectAdminDTO registeredUser = adminService.registerAdmin(user);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (Exception e) {
            // If it's a validation exception related to the name field
            if (e.getMessage() != null && e.getMessage().contains("Name must contain only letters and spaces")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Numbers are not allowed in name. Please use only letters and spaces.");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Registration failed: " + e.getMessage());
        }
    }

    /**
     * Unified Login Endpoint for Admins and Team Members and team leader
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest request) {
        try {
            // First, check if the email exists
            try {
                userDetailsService.loadUserByUsername(request.getEmail());
            } catch (UsernameNotFoundException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email, Please enter valid email");
            }
            
            // If email exists, try authenticating (this will check the password)
            try {
                this.authenticate(request.getEmail(), request.getPassword());
            } catch (BadCredentialsException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password, Please enter valid password");
            }

            // If authentication successful, proceed with token generation
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
        } catch (RequestNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login");
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
        if (teamLeaderRepository.findByEmail(email).isPresent()) {
            return UserRole.TEAM_LEADER;
        }

        if (teamMemberRepository.findByEmail(email).isPresent()) {
            return UserRole.TEAM_MEMBER;
        }

        if (projectAdminRepo.findByEmail(email).isPresent()) {
            return UserRole.ADMIN;
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}