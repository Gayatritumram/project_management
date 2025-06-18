package com.backend.project_management.Repository;

import com.backend.project_management.Entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
        Optional<TeamMember> findByEmail(String email);
        boolean existsByEmail(String email);
    Optional<TeamMember> findByName(String name);

    List<TeamMember> findAllByBranchCode(String branchCode);



}
