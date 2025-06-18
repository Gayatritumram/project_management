package com.backend.project_management.Repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.backend.project_management.Entity.TeamLeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamLeaderRepository extends JpaRepository<TeamLeader, Long> {
    Optional<TeamLeader> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<TeamLeader> findByName(String name);
    List<TeamLeader> findAllByBranchCode(String branchCode);

    Optional<TeamLeader> findByName(String name);

}
