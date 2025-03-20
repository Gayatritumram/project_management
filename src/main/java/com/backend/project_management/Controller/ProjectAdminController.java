package com.backend.project_management.Controller;

import com.backend.project_management.DTO.ProjectAdminDTO;
import com.backend.project_management.Entity.ProjectAdmin;
import com.backend.project_management.Mapper.ProjectAdminMapper;
import com.backend.project_management.Service.ProjectAdminService;
import com.backend.project_management.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class ProjectAdminController {
    @Autowired
    private ProjectAdminService adminService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ProjectAdminMapper adminMapper;

    @PostMapping("/register")
    public ResponseEntity<ProjectAdminDTO> register(@RequestBody ProjectAdminDTO adminDTO) {
        ProjectAdmin admin = adminService.registerAdmin(adminDTO);
        return ResponseEntity.ok(adminMapper.toDTO(admin));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        Optional<ProjectAdmin> admin = adminService.findAdminByEmail(email);

        if (admin.isPresent() && passwordEncoder.matches(password, admin.get().getPassword())) {
            String token = jwtUtil.generateToken(email);
            return ResponseEntity.ok("Bearer " + token);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

}
