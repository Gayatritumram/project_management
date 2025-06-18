package com.backend.project_management.Controller;

import com.backend.project_management.Entity.BranchAdmin;
import com.backend.project_management.Service.BranchAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://pjsofttech.in")
public class BranchAdminController {
    @Autowired
    private BranchAdminService branchAdminService;

    // GET: Get all Branch Admins
    @GetMapping("/getAllBranchAdmins")
    public ResponseEntity<List<BranchAdmin>> getAllBranchAdmins( @RequestParam String role,
                                                                 @RequestParam String email,
                                                                 @RequestParam String branchCode) {


            List<BranchAdmin> list = branchAdminService.getAllBranchAdmins(role,email,branchCode);
            return ResponseEntity.ok(list);

    }

    // GET: Get Branch Admin by Branch Code
    @GetMapping("/getAdminByBranchCode/{branchCode}")
    public ResponseEntity<BranchAdmin> getByBranchCode(@PathVariable String branchCode,
                                                       @RequestParam String role,
                                                       @RequestParam String email) {

            BranchAdmin admin = branchAdminService.getByBranchCode(branchCode,role,email);
            return ResponseEntity.ok(admin);

    }

    // GET: Get Branch Admin by Branch Email
    @GetMapping("/getAdminByBranchEmail/{emailFind}")
    public ResponseEntity<BranchAdmin> getByBranchEmail(@PathVariable String emailFind,
                                                        @RequestParam String role,
                                                        @RequestParam String email) {
        BranchAdmin admin = branchAdminService.getByBranchEmail(emailFind,role,email);
        return ResponseEntity.ok(admin);
    }
}

