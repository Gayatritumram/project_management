package com.backend.project_management.Entity;

import com.backend.project_management.UserPermission.UserRole;
import jakarta.persistence.*;
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

@Entity
@Table(name = "PMProject_admin")
public class ProjectAdmin implements UserDetails {
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


    @Enumerated(EnumType.STRING)
    private UserRole userRole1 = UserRole.ADMIN;

    @OneToMany(mappedBy = "assignedByAdmin", cascade = CascadeType.ALL)
    private List<Task> assignedTasks;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

       return userRole1 != null ?
                List.of(new SimpleGrantedAuthority("ROLE_" + userRole1.name())) :
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public String getUsername() {
        return email;
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


