package com.backend.project_management.Entity;

import com.backend.project_management.UserPermission.UserRole;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
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
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "TeamMember_table")
public class TeamMember implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    private LocalDate joinDate;
    private String department;
    private String phone;
    private String address;
    private String roleName;
    private String projectName;
    private String branchName;
    private boolean isLeader = false;
    //default value is false

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public boolean isLeader() {
        return this.userRole == UserRole.TEAM_LEADER;
    }

    @ManyToOne
    @JoinColumn(name = "team_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
    private Team team;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRole != null ?
                List.of(new SimpleGrantedAuthority("ROLE_" + userRole.name())) :
                List.of(new SimpleGrantedAuthority("ROLE_TEAM_MEMBER"));
    }

    @Override
    public String getUsername() {
//
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}

