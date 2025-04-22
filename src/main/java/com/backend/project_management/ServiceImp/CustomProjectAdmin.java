package com.backend.project_management.ServiceImp;

import com.backend.project_management.Entity.ProjectAdmin;
import com.backend.project_management.Entity.TeamLeader;
import com.backend.project_management.Entity.TeamMember;
import com.backend.project_management.Repository.ProjectAdminRepo;
import com.backend.project_management.Repository.TeamLeaderRepository;
import com.backend.project_management.Repository.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomProjectAdmin implements UserDetailsService {

    @Autowired
    private ProjectAdminRepo projectAdminRepo;

    @Autowired
    private TeamMemberRepository teamMemberRepository;


    @Autowired
    private TeamLeaderRepository teamLeaderRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Check ProjectAdmin
        ProjectAdmin admin = projectAdminRepo.findByEmail(username).orElse(null);
        if (admin != null) {
            if (admin.getUserRole1() == null) {
                throw new UsernameNotFoundException("Project Admin role is missing: " + username);
            }
            return admin;
        }

        // Check TeamLeader
        TeamLeader leader = teamLeaderRepository.findByEmail(username).orElse(null);
        if (leader != null) {
            if (leader.getUserRole() == null) {
                throw new UsernameNotFoundException("Team Leader role is missing: " + username);
            }
            return leader;
        }

        // Check TeamMember
        TeamMember member = teamMemberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        if (member.getUserRole() == null) {
            throw new UsernameNotFoundException("Team Member role is missing: " + username);
        }

        return member;
    }
}


