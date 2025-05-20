package com.backend.project_management.Controller;

import com.backend.project_management.DTO.RoleDTO;
import com.backend.project_management.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@CrossOrigin(origins = "http://localhost:3000")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO,
                                              @RequestParam String role,
                                              @RequestParam String email) {
        return ResponseEntity.ok(roleService.createRole(roleDTO,role,email));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<RoleDTO>> getAllRoles(@RequestParam String role,
                                                     @RequestParam String email,
                                                     @RequestParam String branchCode) {
        return ResponseEntity.ok(roleService.getAllRoles(role, email, branchCode));
    }

    @GetMapping("getRoleById/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id,
                                               @RequestParam String role,
                                               @RequestParam String email) {
        return ResponseEntity.ok(roleService.getRoleById(id,role,email));
    }

    @DeleteMapping("deleteRole/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id,
                                             @RequestParam String role,
                                             @RequestParam String email) {
        roleService.deleteRole(id,role,email);
        return ResponseEntity.ok("Role deleted successfully");
    }
}
