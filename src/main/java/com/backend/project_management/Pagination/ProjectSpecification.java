package com.backend.project_management.Pagination;

import com.backend.project_management.DTO.ProjectDTO;
import com.backend.project_management.Entity.Project;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProjectSpecification {

    public static Specification<Project> filterBy(ProjectDTO filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by project name (case-insensitive, partial match)
            if (filter.getProjectName() != null && !filter.getProjectName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("projectName")), "%" + filter.getProjectName().toLowerCase() + "%"));
            }

            if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("status")), filter.getStatus().toLowerCase()));
            }

            if (filter.getBranchCode() != null && !filter.getBranchCode().isEmpty()) {
                predicates.add(cb.equal(root.get("branchCode"), filter.getBranchCode()));
            }

            if (filter.getDepartmentName() != null && !filter.getDepartmentName().isEmpty()) {
                predicates.add(cb.equal(root.get("departmentName"), filter.getDepartmentName()));
            }

            if (filter.getTeam1() != null) {
                predicates.add(cb.equal(root.get("team1").get("id"), filter.getTeam1()));
            }

            if (filter.getStartDate() != null && filter.getEndDate() != null) {
                predicates.add(cb.between(root.get("startDate"), filter.getStartDate(), filter.getEndDate()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
