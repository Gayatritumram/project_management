package com.backend.project_management.Configuration;

import com.backend.project_management.Util.JWTAuthenticationFilter;
import com.backend.project_management.Util.JwtEntryPoint;
import io.jsonwebtoken.security.Keys;
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
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.nio.charset.StandardCharsets;

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

    private static final String SECRET_KEY = "your_very_secret_key_here";

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/admin/**").hasRole(ADMIN.name())
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers(GET,"/admin/**").hasAuthority(ADMIN_READ.name())
                        .requestMatchers(POST,"/admin/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT,"/admin/**").hasAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(DELETE,"/admin/**").hasAuthority(ADMIN_DELETE.name())
                        .requestMatchers("/team-members/**").hasAnyRole(ADMIN.name(),TEAM_LEADER.name())
                        .requestMatchers(GET,"/team-members/**").hasAnyAuthority(ADMIN_READ.name(),TEAM_LEADER_READ.name())
                        .requestMatchers(POST,"/team-members/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT,"/team-members/**").hasAnyAuthority(ADMIN_UPDATE.name(),TEAM_LEADER_UPDATE.name())
                        .requestMatchers(DELETE,"/team-members/**").hasAuthority(ADMIN_DELETE.name())
                        .requestMatchers("/tasks/**").hasAnyRole(ADMIN.name(),TEAM_LEADER.name(),TEAM_MEMBER.name())
                        .requestMatchers(GET,"/tasks/**").hasAnyAuthority(ADMIN_READ.name(),TEAM_LEADER_READ.name(),TEAM_MEMBER_READ.name())
                        .requestMatchers(POST,"/tasks/**").hasAnyAuthority(ADMIN_CREATE.name(), TEAM_LEADER_CREATE.name())
                        .requestMatchers(PUT,"/tasks/**").hasAnyAuthority(ADMIN_UPDATE.name(),TEAM_LEADER_UPDATE.name())
                        .requestMatchers(DELETE,"/tasks/**").hasAnyAuthority(ADMIN_DELETE.name(),TEAM_LEADER_DELETE.name())
                        .requestMatchers("/team-leader/**").hasAnyRole(ADMIN.name(),TEAM_LEADER.name(),TEAM_MEMBER.name())
                        .requestMatchers(GET,"/team-leader/**").hasAnyAuthority(ADMIN_READ.name(),TEAM_LEADER_READ.name(),TEAM_MEMBER_READ.name())
                        .requestMatchers(POST,"/team-leader/**").hasAnyAuthority(ADMIN_CREATE.name(), TEAM_LEADER_CREATE.name())
                        .requestMatchers(PUT,"/team-leader/**").hasAnyAuthority(ADMIN_UPDATE.name(),TEAM_LEADER_UPDATE.name())
                        .requestMatchers(DELETE,"/team-leader/**").hasAnyAuthority(ADMIN_DELETE.name(),TEAM_LEADER_DELETE.name())
                        .requestMatchers("/Project/**").hasAnyRole(ADMIN.name(),TEAM_LEADER.name(),TEAM_MEMBER.name())
                        .requestMatchers(GET,"/Project/**").hasAnyAuthority(ADMIN_READ.name(),TEAM_LEADER_READ.name(),TEAM_MEMBER_READ.name())
                        .requestMatchers(POST,"/Project/**").hasAnyAuthority(ADMIN_CREATE.name(), TEAM_LEADER_CREATE.name())
                        .requestMatchers(PUT,"/Project/**").hasAnyAuthority(ADMIN_UPDATE.name(),TEAM_LEADER_UPDATE.name())
                        .requestMatchers(DELETE,"/Project/**").hasAnyAuthority(ADMIN_DELETE.name(),TEAM_LEADER_DELETE.name())
                        .requestMatchers("/branches/**").hasRole(ADMIN.name())
                        .requestMatchers(GET,"/branches/**").hasAuthority(ADMIN_READ.name())
                        .requestMatchers(POST,"/branches/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT,"/branches/**").hasAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(DELETE,"/branches/**").hasAuthority(ADMIN_DELETE.name())
                        .requestMatchers("/departments/**").hasRole(ADMIN.name())
                        .requestMatchers(GET,"/departments/**").authenticated()
                        .requestMatchers(POST,"/departments/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT,"/departments/**").hasAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(DELETE,"/departments/**").hasAuthority(ADMIN_DELETE.name())
                        .requestMatchers("/roles/**").hasRole(ADMIN.name())
                        .requestMatchers(GET,"/roles/**").hasAuthority(ADMIN_READ.name())
                        .requestMatchers(POST,"/roles/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT,"/roles/**").hasAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(DELETE,"/roles/**").hasAuthority(ADMIN_DELETE.name())
                        .requestMatchers("/teams/**").hasAnyRole(ADMIN.name(),TEAM_LEADER.name(),TEAM_MEMBER.name())
                        .requestMatchers(GET,"/teams/**").hasAnyAuthority(ADMIN_READ.name(),TEAM_LEADER.name(),TEAM_MEMBER.name())
                        .requestMatchers(POST,"/teams/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT,"/teams/**").hasAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(DELETE,"/teams/**").hasAuthority(ADMIN_DELETE.name())
                        .requestMatchers("/forgetPassword/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("userRole");
        grantedAuthoritiesConverter.setAuthorityPrefix(""); // ← REMOVE prefixing

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return converter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        String secret = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";
        return NimbusJwtDecoder.withSecretKey(
                        Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .macAlgorithm(MacAlgorithm.HS512)  // Important!
                .build();
    }
}
