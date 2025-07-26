package com.backend.project_management.Controller;

import com.backend.project_management.DTO.BranchResponseDTO;
import com.backend.project_management.Service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branch")
@CrossOrigin(origins = "http://localhost:5173")
//@CrossOrigin(origins = "https://pjsofttech.in")
public class BranchController {
    @Autowired
    private BranchService branchService;


    @PostMapping("/createBranch")
    public ResponseEntity<BranchResponseDTO> createBranchName(@RequestBody BranchResponseDTO branchResponseDTO,
                                                        @RequestParam String role,
                                                        @RequestParam String email) {
        return ResponseEntity.ok(branchService.CreateBranchName(branchResponseDTO, role, email));
    }
    @PutMapping("/updateBranch/{id}")
    public ResponseEntity<BranchResponseDTO>updateBranchName(@PathVariable Long id,
                                                         @RequestBody BranchResponseDTO branchResponseDTO,
                                                         @RequestParam String role,
                                                         @RequestParam String email){
        return ResponseEntity.ok(branchService.UpdateBranchName(id,branchResponseDTO,role,email));
    }
    @GetMapping("/getBranchById/{id}")
    public ResponseEntity<BranchResponseDTO> getBranchNameById(@PathVariable Long id,
                                                 @RequestParam String role,
                                                 @RequestParam String email) {
        return ResponseEntity.ok(branchService.getBranchNameById(id, role, email));
    }
    @GetMapping("/getAllBranch")
    public ResponseEntity<List<BranchResponseDTO>> getAllBranchName(@RequestParam String role,
                                                      @RequestParam String email,
                                                      @RequestParam String branchCode) {
        return ResponseEntity.ok(branchService.getAllBranchesName(role, email, branchCode));
    }
    @DeleteMapping("/deleteBranchById/{id}")
    public ResponseEntity<String> deleteBranchById(@PathVariable Long id,
                                         @RequestParam String role,
                                         @RequestParam String email) {
        branchService.deleteBranchName(id, role, email);
        return ResponseEntity.ok("Branch with ID " + id + " deleted successfully.");
    }

}
