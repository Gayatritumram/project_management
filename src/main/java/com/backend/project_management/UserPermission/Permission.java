package com.backend.project_management.UserPermission;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    TEAM_LEADER_READ("TeamLeader:read"),
    TEAM_LEADER_UPDATE("TeamLeader:update"),
    TEAM_LEADER_CREATE("TeamLeader:create"),
    TEAM_LEADER_DELETE("TeamLeader:delete"),
    TEAM_MEMBER_READ("TeamMember:read"),

    ;

    private final String permission;
}
