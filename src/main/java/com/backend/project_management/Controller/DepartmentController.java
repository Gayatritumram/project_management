package com.backend.project_management.Controller;

import com.backend.project_management.DTO.DepartmentDTO;
import com.backend.project_management.Service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
@CrossOrigin(origins = "http://localhost:3000")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;


    @PostMapping("/createDepartment")
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody DepartmentDTO departmentDTO,
                                                          @RequestParam String email,
                                                          @RequestParam String role){
        DepartmentDTO addDepartment = departmentService.createDepartment(departmentDTO,email,role);

        return new ResponseEntity<DepartmentDTO>(addDepartment, HttpStatus.CREATED);
    }

    @PutMapping("/updateDepartmentById/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartmentById(@RequestBody DepartmentDTO departmentDTO,
                                                              @PathVariable Long id,
                                                              @RequestParam String role,
                                                              @RequestParam String email) {
        return ResponseEntity.ok(departmentService.updateDepartment(id,departmentDTO, email,role));
    }

    @GetMapping("/getDepartmentById/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@RequestParam String role,
                                                           @RequestParam String email,
                                                           @PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id, email,role));
    }

    @GetMapping("/getAllDepartments")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments(@RequestParam String role,
                                                                 @RequestParam String email,
                                                                 @RequestParam String branchCode) {
        return ResponseEntity.ok(departmentService.getAllDepartments(role, email, branchCode));
    }

    @DeleteMapping("/deleteDepartment/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long id,
                                                   @RequestParam String role,
                                                   @RequestParam String email) {
        departmentService.deleteDepartment(id, role, email);
        return ResponseEntity.ok("Department deleted successfully");
    }
}
