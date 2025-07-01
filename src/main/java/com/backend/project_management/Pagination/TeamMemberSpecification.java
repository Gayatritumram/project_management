package com.backend.project_management.Pagination;

import com.backend.project_management.Entity.TeamMember;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class TeamMemberSpecification {

    public static Specification<TeamMember> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<TeamMember> hasBranchName(String branchName) {
        return (root, query, cb) -> {
            if (branchName == null || branchName.isEmpty()) return null;
            return cb.like(cb.lower(root.get("branchName")), "%" + branchName.toLowerCase() + "%");
        };
    }

    public static Specification<TeamMember> hasDepartmentName(String departmentName) {
        return (root, query, cb) -> {
            if (departmentName == null || departmentName.isEmpty()) return null;
            return cb.like(cb.lower(root.get("departmentName")), "%" + departmentName.toLowerCase() + "%");
        };
    }

    public static Specification<TeamMember> hasRoleName(String roleName) {
        return (root, query, cb) -> {
            if (roleName == null || roleName.isEmpty()) return null;
            return cb.like(cb.lower(root.get("roleName")), "%" + roleName.toLowerCase() + "%");
        };
    }

    public static Specification<TeamMember> hasBranchCode(String branchCode) {
        return (root, query, cb) -> {
            if (branchCode == null || branchCode.isEmpty()) return null;
            return cb.equal(root.get("branchCode"), branchCode);
        };
    }
}
