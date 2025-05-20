package com.backend.project_management.Controller;

import com.backend.project_management.DTO.DepartmentDTO;
import com.backend.project_management.Service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
@CrossOrigin(origins = "http://localhost:3000")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/create")
    public ResponseEntity<DepartmentDTO> createDepartment( @RequestBody DepartmentDTO departmentDTO,
                                                           @RequestParam String role,
                                                           @RequestParam String email) {
        return ResponseEntity.ok(departmentService.createDepartment(departmentDTO,role,email));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments(@RequestParam String role,
                                                                 @RequestParam String email,
                                                                 @RequestParam String branchCode) {
        return ResponseEntity.ok(departmentService.getAllDepartments(role, email, branchCode));
    }


    @GetMapping("getDepartmentById/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable Long id,
                                                           @RequestParam String role,
                                                           @RequestParam String email) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id, role, email));
    }



    @DeleteMapping("deleteDepartment/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long id,
                                                   @RequestParam String role,
                                                   @RequestParam String email) {
        departmentService.deleteDepartment(id,role, email);
        return ResponseEntity.ok("Department deleted successfully");
    }

}
