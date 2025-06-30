package com.backend.project_management.Pagination;

import com.backend.project_management.DTO.TaskDTO;
import com.backend.project_management.Entity.Task;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {

    public static Specification<Task> build(TaskDTO filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getBranchCode() != null) {
                predicates.add(cb.equal(root.get("branchCode"), filter.getBranchCode()));
            }
            if (filter.getSubject() != null) {
                predicates.add(cb.like(cb.lower(root.get("subject")), "%" + filter.getSubject().toLowerCase() + "%"));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            if (filter.getPriority() != null) {
                predicates.add(cb.equal(root.get("priority"), filter.getPriority()));
            }
            if (filter.getProjectName() != null) {
                predicates.add(cb.equal(root.get("projectName"), filter.getProjectName()));
            }

            // For entity reference fields like assignedToTeamMember and assignedToTeamLeader
            if (filter.getAssignedToTeamMember() != null) {
                predicates.add(cb.equal(root.get("assignedToTeamMember").get("id"), filter.getAssignedToTeamMember()));
            }

            if (filter.getAssignedToTeamLeader() != null) {
                predicates.add(cb.equal(root.get("assignedToTeamLeader").get("id"), filter.getAssignedToTeamLeader()));
            }

            // Handle time frame filters
            if (filter.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), filter.getStartDate()));
            }

            if (filter.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("endDate"), filter.getEndDate()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
