package com.backend.project_management.Controller;

import com.backend.project_management.DTO.BranchResponseDTO;
import com.backend.project_management.DTO.TeamDTO;
import com.backend.project_management.DTO.TeamLeaderDTO;
import com.backend.project_management.Entity.Branch;
import com.backend.project_management.Service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branch")
public class BranchController {
    @Autowired
    private BranchService branchService;


    @PostMapping("/createBranch")
    public ResponseEntity<BranchResponseDTO> createBranch(@RequestBody BranchResponseDTO branchResponseDTO,
                                                        @RequestParam String role,
                                                        @RequestParam String email) {
        return ResponseEntity.ok(branchService.CreateBranch(branchResponseDTO, role, email));
    }
    @PutMapping("/updateBranch/{id}")
    public ResponseEntity<BranchResponseDTO>updateBranch(@PathVariable Long id,
                                                         @RequestBody BranchResponseDTO branchResponseDTO,
                                                         @RequestParam String role,
                                                         @RequestParam String email){
        return ResponseEntity.ok(branchService.UpdateBranch(id,branchResponseDTO,role,email));
    }
    @GetMapping("/getBranchById/{id}")
    public ResponseEntity<BranchResponseDTO> getBranchById(@PathVariable Long id,
                                                 @RequestParam String role,
                                                 @RequestParam String email) {
        return ResponseEntity.ok(branchService.getBranchById(id, role, email));
    }
    @GetMapping("/getAllBranch")
    public ResponseEntity<List<BranchResponseDTO>> getAllBranch(@RequestParam String role,
                                                      @RequestParam String email,
                                                      @RequestParam String branchCode) {
        return ResponseEntity.ok(branchService.getAllBranches(role, email, branchCode));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id,
                                         @RequestParam String role,
                                         @RequestParam String email) {
        branchService.deleteBranch(id, role, email);
        return ResponseEntity.ok("Branch with ID " + id + " deleted successfully.");
    }

}
