package com.backend.project_management.Controller;

import com.backend.project_management.DTO.BranchDTO;
import com.backend.project_management.Service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branches")
@CrossOrigin(origins = "http://localhost:3000")
public class BranchController {
    @Autowired
    private BranchService branchService;

    @PostMapping
    public ResponseEntity<BranchDTO> createBranch(@RequestBody BranchDTO branchDTO) {
        return ResponseEntity.ok(branchService.createBranch(branchDTO));
    }

    @GetMapping
    public ResponseEntity<List<BranchDTO>> getAllBranches() {
        return ResponseEntity.ok(branchService.getAllBranches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchDTO> getBranchById(@PathVariable Long id) {
        return ResponseEntity.ok(branchService.getBranchById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.ok("Branch deleted successfully");
    }
}
