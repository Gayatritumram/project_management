package com.backend.project_management.Configuration;

import com.backend.project_management.Util.JWTAuthenticationFilter;
import com.backend.project_management.Util.JwtEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.backend.project_management.UserPermission.Permission.*;
import static com.backend.project_management.UserPermission.UserRole.*;
import static org.springframework.http.HttpMethod.*;

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
                .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF for JWT
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // Public Endpoints (No authentication required)
                        .requestMatchers("/auth/**").permitAll()

                        //ADMIN
                        .requestMatchers("/admin/**").hasRole(ADMIN.name())


                        .requestMatchers(GET,"/admin/**").hasAuthority(ADMIN_READ.name())
                        .requestMatchers(POST,"/admin/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT,"/admin/**").hasAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(DELETE,"/admin/**").hasAuthority(ADMIN_DELETE.name())

                        //TEAM_LEADER
                        .requestMatchers("/team-members/**").hasAnyRole(ADMIN.name(),TEAM_LEADER.name())

                        .requestMatchers(GET,"/team-members/**").hasAnyAuthority(ADMIN_READ.name(),TEAM_LEADER_READ.name())
                        .requestMatchers(POST,"/team-members/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT,"/team-members/**").hasAnyAuthority(ADMIN_UPDATE.name(),TEAM_LEADER_UPDATE.name())
                        .requestMatchers(DELETE,"/team-members/**").hasAuthority(ADMIN_DELETE.name())

                        //Task
                        .requestMatchers("/tasks/**").hasAnyRole(ADMIN.name(),TEAM_LEADER.name(),TEAM_MEMBER.name())

                        .requestMatchers(GET,"/tasks/**").hasAnyAuthority(ADMIN_READ.name(),TEAM_LEADER_READ.name(),TEAM_MEMBER_READ.name())
                        .requestMatchers(POST,"/tasks/**").hasAnyAuthority(ADMIN_CREATE.name(), TEAM_LEADER_CREATE.name())
                        .requestMatchers(PUT,"/tasks/**").hasAnyAuthority(ADMIN_UPDATE.name(),TEAM_LEADER_UPDATE.name())
                        .requestMatchers(DELETE,"/tasks/**").hasAnyAuthority(ADMIN_DELETE.name(),TEAM_LEADER_DELETE.name())

                        //Team Leader

                        .requestMatchers("/team-leader/**").hasAnyRole(ADMIN.name(),TEAM_LEADER.name(),TEAM_MEMBER.name())

                        .requestMatchers(GET,"/team-leader/**").hasAnyAuthority(ADMIN_READ.name(),TEAM_LEADER_READ.name(),TEAM_MEMBER_READ.name())
                        .requestMatchers(POST,"/team-leader/**").hasAnyAuthority(ADMIN_CREATE.name(), TEAM_LEADER_CREATE.name())
                        .requestMatchers(PUT,"/team-leader/**").hasAnyAuthority(ADMIN_UPDATE.name(),TEAM_LEADER_UPDATE.name())
                        .requestMatchers(DELETE,"/team-leader/**").hasAnyAuthority(ADMIN_DELETE.name(),TEAM_LEADER_DELETE.name())

                        //PROJECT

                        .requestMatchers("/Project/**").hasAnyRole(ADMIN.name(),TEAM_LEADER.name(),TEAM_MEMBER.name())

                        .requestMatchers(GET,"/Project/**").hasAnyAuthority(ADMIN_READ.name(),TEAM_LEADER_READ.name(),TEAM_MEMBER_READ.name())
                        .requestMatchers(POST,"/Project/**").hasAnyAuthority(ADMIN_CREATE.name(), TEAM_LEADER_CREATE.name())
                        .requestMatchers(PUT,"/Project/**").hasAnyAuthority(ADMIN_UPDATE.name(),TEAM_LEADER_UPDATE.name())
                        .requestMatchers(DELETE,"/Project/**").hasAnyAuthority(ADMIN_DELETE.name(),TEAM_LEADER_DELETE.name())

                        //BRANCH
                        .requestMatchers("/branches/**").hasRole(ADMIN.name())

                        .requestMatchers(GET,"/branches/**").hasAuthority(ADMIN_READ.name())
                        .requestMatchers(POST,"/branches/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT,"/branches/**").hasAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(DELETE,"/branches/**").hasAuthority(ADMIN_DELETE.name())

                        //DEPARTMENTS
                        .requestMatchers("/departments/**").hasRole(ADMIN.name())

                        .requestMatchers(GET,"/departments/**").authenticated()
                        .requestMatchers(POST,"/departments/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT,"/departments/**").hasAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(DELETE,"/departments/**").hasAuthority(ADMIN_DELETE.name())

                        //ROLE
                        .requestMatchers("/roles/**").hasRole(ADMIN.name())

                        .requestMatchers(GET,"/roles/**").hasAuthority(ADMIN_READ.name())
                        .requestMatchers(POST,"/roles/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT,"/roles/**").hasAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(DELETE,"/roles/**").hasAuthority(ADMIN_DELETE.name())

                        //TEAMS
                        .requestMatchers("/teams/**").hasAnyRole(ADMIN.name(),TEAM_LEADER.name(),TEAM_MEMBER.name())

                        .requestMatchers(GET,"/teams/**").hasAnyAuthority(ADMIN_READ.name(),TEAM_LEADER.name(),TEAM_MEMBER.name())
                        .requestMatchers(POST,"/teams/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT,"/teams/**").hasAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(DELETE,"/teams/**").hasAuthority(ADMIN_DELETE.name())

                        .requestMatchers("/forgetPassword/**").permitAll()





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
