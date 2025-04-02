package com.backend.project_management.UserPermission;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.backend.project_management.UserPermission.Permission.*;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    TEAM_MEMBER(
            Set.of(
            TEAM_MEMBER_READ
         )
    ),

    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    TEAM_LEADER_READ,
                    TEAM_LEADER_UPDATE,
                    TEAM_LEADER_DELETE,
                    TEAM_LEADER_CREATE
            )
    ),
    TEAM_LEADER(
            Set.of(
                    TEAM_LEADER_READ,
                    TEAM_LEADER_UPDATE,
                    TEAM_LEADER_DELETE,
                    TEAM_LEADER_CREATE
            )
    )



    ;

    private final Set<Permission> permissions;



        public List<SimpleGrantedAuthority> getAuthorities() {
            var authorities = getPermissions()
                    .stream()
                    .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                    .collect(Collectors.toList());
            authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
            return authorities;
        }
    }

