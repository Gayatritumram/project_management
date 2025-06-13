package com.backend.project_management.Mapper;

import com.backend.project_management.DTO.BranchResponseDTO;
import com.backend.project_management.Entity.Branch;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class BranchMapper {
    public Branch toEntity(BranchResponseDTO dto) {
        return Branch.builder().branchName(dto.getBranchName()).build();
    }
    public BranchResponseDTO toResponseDTO(Branch branch){
     return BranchResponseDTO.builder().id(branch.getId()).branchName(branch.getBranchName())
             .createdByEmail(branch.getCreatedByEmail()).role(branch.getRole()).branchCode(branch.getBranchCode())
             .createdDate(branch.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
             .build();
    }
}
