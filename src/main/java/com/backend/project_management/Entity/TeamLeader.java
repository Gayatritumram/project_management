package com.backend.project_management.Entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "PMTeamLeader_table")
public class TeamLeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phone;
    private String address;
    private String department;
    private String branchName;
    private LocalDate joinDate;
    private String imageUrl;

    @Email
    private String createdByEmail;

    private String role;

    private String branchCode;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team teamId;


    public boolean isCanAccessTask() {
        return  true;
    }

    public boolean isCanAccessProject() {
        return true;
    }

    public boolean isCanAccessTeamMember() {
        return  true;
    }
}
