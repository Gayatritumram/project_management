package com.backend.project_management.ServiceImp;

import com.backend.project_management.Entity.TeamLeader;
import com.backend.project_management.Entity.TeamMember;
import com.backend.project_management.Repository.ProjectAdminRepo;
import com.backend.project_management.Repository.TeamLeaderRepository;
import com.backend.project_management.Repository.TeamMemberRepository;
import com.backend.project_management.Service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class StaffValidation {

    @Autowired
    private  WebClient webClient;

    @Autowired
    private StaffService staffService;

    @Autowired
    private ProjectAdminRepo projectAdminRepo;

    @Autowired
    private TeamLeaderRepository teamLeaderRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;


    public boolean hasPermission(String role, String email, String action) {
        if ("BRANCH".equalsIgnoreCase(role)) {
            try {
                Boolean exists = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/existBranchbyemail")
                                .queryParam("email", email)
                                .build())
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .block();

                return Boolean.TRUE.equals(exists);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        if (switch (role.toUpperCase()) {
            case "STAFF" -> {
                Map<String, Boolean> perms = staffService.getPermissionsByEmail(email);
                yield switch (action.toUpperCase()) {
                    case "GET" -> Boolean.TRUE.equals(perms.get("cansGet"));
                    case "POST" -> Boolean.TRUE.equals(perms.get("cansPost"));
                    case "PUT" -> Boolean.TRUE.equals(perms.get("cansPut"));
                    case "DELETE" -> Boolean.TRUE.equals(perms.get("cansDelete"));
                    default -> false;
                };
            }
            case "DEPARTMENT" -> {
                Map<String, Object> perms = staffService.getCrudPermissionForDepartmentByEmail(email);
                yield switch (action.toUpperCase()) {
                    case "GET" -> Boolean.TRUE.equals(perms.get("candGet"));
                    case "POST" -> Boolean.TRUE.equals(perms.get("candPost"));
                    case "PUT" -> Boolean.TRUE.equals(perms.get("candPut"));
                    case "DELETE" -> Boolean.TRUE.equals(perms.get("candDelete"));
                    default -> false;
                };
            }
            case "ADMIN" -> {
                yield projectAdminRepo.existsByEmail(email);
            }

            case "TEAM_LEADER" -> {
                // Fetch entity directly (assuming teamLeaderRepository is accessible here)
                TeamLeader leader = teamLeaderRepository.findByEmail(email)
                        .orElse(null);
                if (leader == null) yield false;

                yield switch (action.toUpperCase()) {
                    case "GET" -> true; // full GET access
                    case "POST", "DELETE" -> leader.isCanAccessTask() || leader.isCanAccessProject();
                    case "PUT" -> leader.isCanAccessTask() || leader.isCanAccessProject() || leader.isCanAccessTeamMember();
                    default -> false;
                };
            }

            case "TEAM_MEMBER" -> {
                TeamMember member = teamMemberRepository.findByEmail(email).orElse(null);
                if (member == null) yield false;

                yield switch (action.toUpperCase()) {
                    case "GET" -> true;  // Full GET access
                    case "PUT" -> member.isCanAccessTask();  // Only PUT allowed for task updates if flag true
                    default -> false;
                };
            }

            default -> false;
        }) return true;
        else return false;
    }





    public String fetchBranchCodeByRole(String role, String email) {
        String endpoint = switch (role.toLowerCase()) {
            case "branch" -> "/branch/getbranchcode";
            case "department" -> "/department/getbranchcode";
            case "staff" -> "/staff/getbranchcode";
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        };

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(endpoint)
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
