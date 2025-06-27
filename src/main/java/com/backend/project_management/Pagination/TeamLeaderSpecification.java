package com.backend.project_management.Pagination;


import com.backend.project_management.Entity.TeamLeader;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TeamLeaderSpecification {
    public static Specification<TeamLeader> filter(String branchCode, String searchBy) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (branchCode != null && !branchCode.isEmpty()) {
                predicates.add(cb.equal(root.get("branchCode"), branchCode));
            }

            if (searchBy != null && !searchBy.isEmpty()) {
                Predicate emailMatch = cb.like(root.get("email"), "%" + searchBy + "%");
                Predicate nameMatch = cb.like(cb.lower(root.get("name")), "%" + searchBy.toLowerCase() + "%");
                predicates.add(cb.or(emailMatch, nameMatch));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}




