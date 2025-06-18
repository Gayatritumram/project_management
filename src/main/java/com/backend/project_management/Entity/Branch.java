package com.backend.project_management.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PM_BRANCH")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType. IDENTITY)
    private Long id;
    private String branchName;
    @Email
    private String createdByEmail;
    private String role;
    private String branchCode;


    private LocalDateTime createdDate;
}


