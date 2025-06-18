package com.backend.project_management.Exception;

import com.backend.project_management.Repository.TeamLeaderRepository;
import com.backend.project_management.Repository.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DuplicateEmailHandler {

    @Autowired
    private TeamMemberRepository teamMemberRepository;
    
    @Autowired
    private TeamLeaderRepository teamLeaderRepository;
    
    /**
     * Checks if an email already exists in any user repository
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    public boolean isEmailAlreadyRegistered(String email) {
        return
               teamMemberRepository.findByEmail(email).isPresent() ||
               teamLeaderRepository.findByEmail(email).isPresent();
    }
} 