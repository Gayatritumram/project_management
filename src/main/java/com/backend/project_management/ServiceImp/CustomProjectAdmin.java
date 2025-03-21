package com.backend.project_management.ServiceImp;

import com.backend.project_management.Entity.ProjectAdmin;
import com.backend.project_management.Repository.ProjectAdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomProjectAdmin implements UserDetailsService {


        @Autowired
        private ProjectAdminRepo projectAdminRepo ;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            ProjectAdmin user = projectAdminRepo.findByEmail(username).orElseThrow(() ->new RuntimeException("user not found"));

            return user;
        }
    }

