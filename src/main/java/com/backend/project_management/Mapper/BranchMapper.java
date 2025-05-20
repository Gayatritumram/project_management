package com.backend.project_management.Mapper;

import com.backend.project_management.DTO.BranchDTO;
import com.backend.project_management.Entity.Branch;
import org.springframework.stereotype.Component;

@Component
public class BranchMapper {
    public BranchDTO toDTO(Branch branch) {
        BranchDTO dto = new BranchDTO();
        dto.setId(branch.getId());
        dto.setBranchName(branch.getBranchName());
        dto.setBranchCode(branch.getBranchCode());
        dto.setRole(branch.getRole());
        dto.setCreatedByEmail(branch.getCreatedByEmail());
        return dto;
    }

    public Branch toEntity(BranchDTO dto) {
        Branch branch = new Branch();
        branch.setId(dto.getId());
        branch.setBranchName(dto.getBranchName());
        branch.setBranchCode(dto.getBranchCode());
        branch.setRole(dto.getRole());
        branch.setCreatedByEmail(dto.getCreatedByEmail());
        return branch;
    }
}
