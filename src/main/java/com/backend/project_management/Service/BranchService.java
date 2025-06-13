package com.backend.project_management.Service;

import com.backend.project_management.DTO.BranchResponseDTO;

import java.util.List;

public interface BranchService {
    BranchResponseDTO CreateBranch(BranchResponseDTO branchDTO,String role,String email);

    BranchResponseDTO UpdateBranch(Long id, BranchResponseDTO branchResponseDTO, String role, String email);

    BranchResponseDTO getBranchById(Long id, String role, String email);

    List<BranchResponseDTO> getAllBranches(String role, String email, String branchcode);





}
