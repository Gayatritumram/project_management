package com.backend.project_management.Repository;

import com.backend.project_management.Entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
        List<TeamMember> findByIsLeaderTrue();
        //To get all team leaders
        List<TeamMember> findByIsLeaderFalse();
        //to get all team members who are not leaders

}
