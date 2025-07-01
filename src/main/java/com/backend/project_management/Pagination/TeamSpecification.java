package com.backend.project_management.Pagination;

import com.backend.project_management.Entity.Team;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TeamSpecification {
    public static Specification<Team> filter(Team filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by ID
            if (filter.getId() != null) {
                predicates.add(cb.equal(root.get("id"), filter.getId()));
            }
            // Filter by team name
            if (filter.getTeamName() != null && !filter.getTeamName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("teamName")), "%" + filter.getTeamName().toLowerCase() + "%"));
            }
            // Filter by branch name
            if (filter.getBranchName() != null && !filter.getBranchName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("branchName")), "%" + filter.getBranchName().toLowerCase() + "%"));
            }
            // Filter by department name
            if (filter.getDepartmentName() != null && !filter.getDepartmentName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("departmentName")), "%" + filter.getDepartmentName().toLowerCase() + "%"));
            }
            // Filter by created by email
            if (filter.getCreatedByEmail() != null && !filter.getCreatedByEmail().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("createdByEmail")), "%" + filter.getCreatedByEmail().toLowerCase() + "%"));
            }
            // Filter by role
            if (filter.getRole() != null && !filter.getRole().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("role")), filter.getRole().toLowerCase()));
            }
            // Filter by branch code
            if (filter.getBranchCode() != null && !filter.getBranchCode().isEmpty()) {
                predicates.add(cb.equal(root.get("branchCode"), filter.getBranchCode()));
            }

            // Combine all predicates
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}