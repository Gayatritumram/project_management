package com.backend.project_management.Repository;

import com.backend.project_management.Entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findAllByBranchCode(String branchCode);
}
