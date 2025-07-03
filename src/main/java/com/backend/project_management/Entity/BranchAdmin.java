package com.backend.project_management.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "PM_BranchAdmin")
public class BranchAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, unique = true)
    private String email;

    private Long bid;
    private String branchCode;

    private String branchName;

    @Email
    private String branchEmail;
    @Email
    private String instituteEmail;
    private String systems;

    private LocalDateTime LoginDateTime;

}
