package com.backend.project_management.Repository;

import com.backend.project_management.Entity.BranchAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchAdminRepository extends JpaRepository<BranchAdmin, Long> {
    Optional<BranchAdmin> findByBranchCode(String branchCode);
    Optional<BranchAdmin> findByBranchEmail(String branchEmail);
}
