package com.backend.project_management.Entity;

import com.backend.project_management.UserPermission.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "PMProject_admin")
public class ProjectAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Numbers are not allowed in name. Please use only letters and spaces.")
    private String name;

    @Column(nullable = false, unique = true)
    private String email;
    private String phone;

    @Column(nullable = false)
    private String password;
    @Email
    private String createdByEmail;
    private String role;
    private String branchCode;

}


