package com.backend.project_management.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BranchData {
    private Long id;
    private Long bid;
    private String branchCode;
    private String branchName;
    private String branchEmail;
    private String instituteEmail;
    private List<SystemDTO> systems;
}
