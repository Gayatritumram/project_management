package com.backend.project_management.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PM_Department")
public class Department {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String departmentName;

        @Email
        private String createdByEmail;

        private String role;

        private String branchCode;

        private LocalDateTime createdDate;

    }