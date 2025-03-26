package com.backend.project_management.ServiceImp;

import com.backend.project_management.Entity.ProjectAdmin;
import com.backend.project_management.Repository.ProjectAdminRepo;
import com.backend.project_management.Repository.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomProjectAdmin implements UserDetailsService {


        @Autowired
        private ProjectAdminRepo projectAdminRepo ;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ProjectAdmin user = projectAdminRepo.findByEmail(username).orElse(null);
        if (user != null) {
            return user; // Return user if found
        }

        // Try to find the user in teamMemberRepository
        return teamMemberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
    }

