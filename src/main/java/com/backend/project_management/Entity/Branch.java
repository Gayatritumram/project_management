package com.backend.project_management.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType. IDENTITY)
    private Long id;
    private String branchName;
    private String createdByEmail;
    private String role;
    private String branchCode;


    private LocalDateTime createdDate;
}


