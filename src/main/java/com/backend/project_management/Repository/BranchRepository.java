package com.backend.project_management.Repository;

import com.backend.project_management.Entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {
}
