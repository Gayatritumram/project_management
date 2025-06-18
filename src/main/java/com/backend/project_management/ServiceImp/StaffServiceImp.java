package com.backend.project_management.ServiceImp;


import com.backend.project_management.Entity.TeamLeader;
import com.backend.project_management.Entity.TeamMember;
import com.backend.project_management.Model.JwtRequest;
import com.backend.project_management.Model.JwtResponse;
import com.backend.project_management.Repository.TeamLeaderRepository;
import com.backend.project_management.Repository.TeamMemberRepository;
import com.backend.project_management.Service.BranchAdminService;
import com.backend.project_management.Service.StaffService;
import com.backend.project_management.Util.JwtHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class StaffServiceImp implements StaffService {

    private final WebClient webClient;

    @Autowired
    private TeamLeaderRepository teamLeaderRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private BranchAdminService branchAdminService;

    @Autowired
    public StaffServiceImp(WebClient webClient) {
        this.webClient = webClient;
    }



    @Override
    public Mono<JwtResponse> loginStaff(JwtRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        Map<String, Object> userData = new HashMap<>();

        // 1. Try Branch Login via external service

        return webClient.post()
                .uri("/branchlogin")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new RuntimeException("Branch Login Failed: " + error)))
                )
                .bodyToMono(JwtResponse.class)
                .flatMap(jwtResponse -> {
                    Object data = jwtResponse.getData();
                    if (jwtResponse.getToken() != null && data instanceof Map) {
                        Map<String, Object> dataMap = (Map<String, Object>) data;
                        if (dataMap.containsKey("branchEmail")) {
                            try {
                                branchAdminService.saveBranchAdmin(jwtResponse);
                            } catch (Exception e) {
                                System.out.println("Failed to save branch admin: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Skipping save: branchEmail not found in data.");
                        }
                    } else {
                        System.out.println("Skipping save: token is null or data is not a valid map.");
                    }

                    return Mono.just(jwtResponse);
                })


        // 2. If branch login fails, try TeamLeader
                .onErrorResume(branchError -> {
                    Optional<TeamLeader> leaderOpt = teamLeaderRepository.findByEmail(email);
                    if (leaderOpt.isPresent()) {
                        TeamLeader leader = leaderOpt.get();
                        if (!passwordEncoder.matches(password, leader.getPassword())) {
                            return Mono.error(new RuntimeException("Invalid email or password"));
                        }

                        String token = jwtHelper.generateToken(email);
                        userData.put("id", leader.getId());
                        userData.put("name", leader.getName());
                        userData.put("email", leader.getEmail());
                        userData.put("role", leader.getRole());
                        userData.put("branchCode", leader.getBranchCode());

                        return Mono.just(new JwtResponse(token, userData));
                    }

                    // 3. If not TeamLeader, try TeamMember
                    Optional<TeamMember> memberOpt = teamMemberRepository.findByEmail(email);
                    if (memberOpt.isPresent()) {
                        TeamMember member = memberOpt.get();
                        if (!passwordEncoder.matches(password, member.getPassword())) {
                            return Mono.error(new RuntimeException("Invalid email or password"));
                        }

                        String token = jwtHelper.generateToken(email);
                        userData.put("id", member.getId());
                        userData.put("name", member.getName());
                        userData.put("email", member.getEmail());
                        userData.put("role", member.getRole());
                        userData.put("branchCode", member.getBranchCode());

                        return Mono.just(new JwtResponse(token, userData));
                    }

                    return Mono.error(new RuntimeException("Invalid email or password"));
                });
    }



    @Override
    public Map<String, Boolean> getPermissionsByEmail(String email) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/permissionForStaff")
                        .queryParam("staffEmail", email)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, token)  // pass it as-is
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Boolean>>() {})
                .block();
    }



    @Override
    public Map<String, Object> getCrudPermissionForDepartmentByEmail(String email) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/permissionForDepartment")
                        .queryParam("email", email)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, token)  // Pass token directly
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}
