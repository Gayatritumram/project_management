package com.backend.project_management.Repository;

import com.backend.project_management.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findAllByBranchCode(String branchCode);
}
