package com.backend.project_management.Controller;
import com.backend.project_management.DTO.ProjectAdminDTO;
import com.backend.project_management.Model.JwtRequest;
import com.backend.project_management.Model.JwtResponse;
import com.backend.project_management.Service.ProjectAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class ProjectAdminController {
    @Autowired
    private ProjectAdminService adminService;

    @PostMapping("/registerAdmin")
    public ResponseEntity<ProjectAdminDTO> registerAdmin(@RequestBody ProjectAdminDTO adminDTO,
                                                         @RequestParam String role,
                                                         @RequestParam String email) {
        ProjectAdminDTO registeredAdmin = adminService.registerAdmin(adminDTO, role, email);
        return ResponseEntity.ok(registeredAdmin);
    }

    @GetMapping("/getAllProjectAdmin")
    public ResponseEntity<List<ProjectAdminDTO>> getAllProjectAdmin(@RequestParam String role,
                                                                    @RequestParam String email,
                                                                    @RequestParam String branchCode) {
        return ResponseEntity.ok(adminService.findAllAdmin(role, email, branchCode));
    }

    @GetMapping("/getProjectAdminByEmail/{emailFind}")
    public ResponseEntity <ProjectAdminDTO> getProjectAdminByEmail(@PathVariable String emailFind,
                                                                    @RequestParam String role,
                                                                    @RequestParam String email) {
        return ResponseEntity.ok(adminService.findAdminByEmail(role, emailFind,email));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id,
                                         @RequestParam String role,
                                         @RequestParam String email) {
        adminService.deleteProjectAdmin(id,role,email);
        return ResponseEntity.ok("Admin with ID " + id + " deleted successfully.");
    }






}
