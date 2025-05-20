package com.backend.project_management.Controller;

import com.backend.project_management.DTO.BranchDTO;
import com.backend.project_management.Service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/branches")
public class BranchController {
    @Autowired
    private BranchService branchService;

    @PostMapping("/create")
    public ResponseEntity<BranchDTO> createBranch(@RequestBody BranchDTO branchDTO,
                                                  @RequestParam String role,
                                                  @RequestParam String email) {
        return ResponseEntity.ok(branchService.createBranch(branchDTO,role,email));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<BranchDTO>> getAllBranches(@RequestParam String role,
                                                          @RequestParam String email,
                                                          @RequestParam String branchCode) {
        return ResponseEntity.ok(branchService.getAllBranches(role, email, branchCode));
    }

    @GetMapping("getBranchById/{id}")
    public ResponseEntity<BranchDTO> getBranchById(@PathVariable Long id,
                                                   @RequestParam String role,
                                                   @RequestParam String email) {
        return ResponseEntity.ok(branchService.getBranchById(id,role,email));
    }

    @DeleteMapping("deleteBranch/{id}")
    public ResponseEntity<String> deleteBranch(@PathVariable Long id,
                                               @RequestParam String role,
                                               @RequestParam String email) {
        branchService.deleteBranch(id,role,email);
        return ResponseEntity.ok("Branch deleted successfully");
    }
}
