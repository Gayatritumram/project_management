package com.backend.project_management.Configuration;

import com.backend.project_management.Util.JWTAuthenticationFilter;
import com.backend.project_management.Util.JwtEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtEntryPoint point;
    @Autowired
    private JWTAuthenticationFilter filter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Bean

    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) .cors(cons -> cons.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/Create/**").authenticated()
                        // Public Endpoints (No authentication required)
                        .requestMatchers("/admin/**").permitAll()
                        // Task Management (Admin and Team Leader can assign tasks)
                        .requestMatchers("/tasks/**").permitAll()
                        // Team Management (Admin can create, Team Leaders can view)
                        .requestMatchers("/team-members/**").permitAll()
                        .requestMatchers("/Project/**").permitAll()
                        .requestMatchers("/branches/**").permitAll()
                        .requestMatchers("/departments/**").permitAll()
                        .requestMatchers("/roles/**").permitAll()
                        .requestMatchers("/api/tasks/**").permitAll()
                        .requestMatchers("/teams/**").permitAll()
                        .requestMatchers("/team-members").permitAll()

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();


    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;


    }


}
