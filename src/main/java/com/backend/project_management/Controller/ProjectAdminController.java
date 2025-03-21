package com.backend.project_management.Controller;

import com.backend.project_management.DTO.ProjectAdminDTO;
import com.backend.project_management.DTO.TeamDTO;
import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Entity.ProjectAdmin;
import com.backend.project_management.Entity.TeamMember;
import com.backend.project_management.Mapper.ProjectAdminMapper;
import com.backend.project_management.Service.ProjectAdminService;
import com.backend.project_management.Service.TeamMemberService;
import com.backend.project_management.Service.TeamService;
import com.backend.project_management.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/admin")
public class ProjectAdminController {
    private final ProjectAdminService adminService;

    public ProjectAdminController(ProjectAdminService adminService) {
        this.adminService = adminService;
    }

    // ✅ Register a new Project Admin
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ProjectAdminDTO adminDTO) {
        try {
            return ResponseEntity.ok(adminService.registerAdmin(adminDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ Login and Get JWT Token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        try {
            String token = adminService.loginAdmin(email, password);
            return ResponseEntity.ok("Bearer " + token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }


}
