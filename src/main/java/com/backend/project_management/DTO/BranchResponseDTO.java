package com.backend.project_management.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BranchResponseDTO {
    private Long id;
    private String branchName;
    private String createdByEmail;
    private String role;
    private String branchCode;
    private String createdDate;
}
