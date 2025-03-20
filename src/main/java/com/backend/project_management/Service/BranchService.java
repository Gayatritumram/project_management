package com.backend.project_management.Service;

import com.backend.project_management.DTO.BranchDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BranchService {
    BranchDTO createBranch(BranchDTO branchDTO);
    List<BranchDTO> getAllBranches();
    BranchDTO getBranchById(Long id);
    void deleteBranch(Long id);
}
