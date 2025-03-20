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

@RestController
@RequestMapping("/admin")
public class ProjectAdminController {
    @Autowired
    private ProjectAdminService adminService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ProjectAdminMapper adminMapper;

    @Autowired
    private TeamMemberService teamService;

    @PostMapping("/register")
    public ResponseEntity<ProjectAdminDTO> register(@RequestBody ProjectAdminDTO adminDTO) {
        ProjectAdmin admin = adminService.registerAdmin(adminDTO);
        return ResponseEntity.ok(adminMapper.toDTO(admin));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        ProjectAdminDTO admin = adminService.findAdminByEmail(email);

        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            String token = jwtUtil.generateToken(email);
            return ResponseEntity.ok("Bearer " + token);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    @GetMapping("/{email}")
    public ResponseEntity<ProjectAdminDTO> getAdminByEmail(@PathVariable String email) {
        ProjectAdminDTO admin = adminService.findAdminByEmail(email);

        return ResponseEntity.ok(admin);
    }

    @PostMapping("/create-team-leader")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeamMemberDTO> createTeamLeader(@RequestBody TeamMemberDTO teamMemberDTO) {
        return ResponseEntity.ok(teamService.createTeamMember(teamMemberDTO));
    }

    @PostMapping("/create-team-member")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeamMemberDTO> createTeamMember(@RequestBody TeamMemberDTO teamMemberDTO) {
        return ResponseEntity.ok(teamService.createTeamMember(teamMemberDTO));
    }


}
