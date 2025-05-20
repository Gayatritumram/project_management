package com.backend.project_management.Service;

import com.backend.project_management.DTO.BranchDTO;

import java.util.List;

public interface BranchService {
    BranchDTO createBranch(BranchDTO branchDTO,String role,String email);
    List<BranchDTO> getAllBranches(String role, String email,String branchCode);
    BranchDTO getBranchById(Long id,String role,String gmail);
    void deleteBranch(Long id,String role,String email);
}
