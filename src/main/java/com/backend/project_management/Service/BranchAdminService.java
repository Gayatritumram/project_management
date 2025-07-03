package com.backend.project_management.Service;

import com.backend.project_management.Entity.BranchAdmin;
import com.backend.project_management.Model.JwtResponse;

import java.util.List;


public interface BranchAdminService {
         void saveBranchAdmin(JwtResponse jwtResponse);

         List<BranchAdmin> getAllBranchAdmins(String role,String email,String branchCode);

         BranchAdmin getByBranchCode(String branchCode,String role,String email);

         BranchAdmin getByBranchEmail(String emailFind,String role,String email);
}
