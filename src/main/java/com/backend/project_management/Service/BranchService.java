package com.backend.project_management.Service;

import com.backend.project_management.DTO.BranchResponseDTO;

import java.util.List;

public interface BranchService {
    BranchResponseDTO CreateBranchName(BranchResponseDTO branchDTO,String role,String email);

    BranchResponseDTO UpdateBranchName(Long id, BranchResponseDTO branchResponseDTO, String role, String email);

    BranchResponseDTO getBranchNameById(Long id, String role, String email);

    List<BranchResponseDTO> getAllBranchesName(String role, String email, String branchcode);

    void deleteBranchName(Long id,String role, String email);



}
