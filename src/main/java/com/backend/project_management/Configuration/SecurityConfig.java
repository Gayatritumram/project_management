package com.backend.project_management.Configuration;

import com.backend.project_management.Exception.CustomAuthenticationEntryPoint;
import com.backend.project_management.Util.JWTAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

        private final JWTAuthenticationFilter jwtAuthFilter; // Inject JwtAuthFilter

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }
        @Autowired
        private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;


        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable()) // Disable CSRF
                    .cors(cors -> cors.configurationSource(request -> {
                        var config = new org.springframework.web.cors.CorsConfiguration();
                        config.setAllowedOrigins(List.of("http://localhost:5173","https://pjsofttech.in")); // Frontend origin
                        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE","PATCH","OPTIONS")); // HTTP methods
                        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                        config.setAllowCredentials(true);
                        return config;
                    }))
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(
                                "/loginBranch",
                                "/admin/adminLogin",
                                "/api/tasks/create/**"
                            ).permitAll()
                            .anyRequest().authenticated()
                    )
                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .exceptionHandling(ex -> ex
                            .authenticationEntryPoint(customAuthenticationEntryPoint) // <-- added line
                    )
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                    .httpBasic(httpBasic -> httpBasic.disable());

            return http.build();
        }

    }






