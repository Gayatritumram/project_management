package com.backend.project_management.Util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        
        // Skip filter for login endpoint
        if (request.getRequestURI().contains("/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Only check if not already authenticated
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            // Extract credentials from request if present
            String email = request.getParameter("email");
            
            if (email != null) {
                try {
                    // Check if user exists
                    userDetailsService.loadUserByUsername(email);
                } catch (UsernameNotFoundException e) {
                    // User not found - set attribute for EntryPoint to use
                    request.setAttribute("auth_error_message", "Invalid email, Please enter valid email");
                }
            }
        }
        
        filterChain.doFilter(request, response);
    }
} 