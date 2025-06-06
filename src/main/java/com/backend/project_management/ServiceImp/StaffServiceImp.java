package com.backend.project_management.ServiceImp;


import com.backend.project_management.Model.JwtRequest;
import com.backend.project_management.Model.JwtResponse;
import com.backend.project_management.Service.StaffService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class StaffServiceImp implements StaffService {

    private final WebClient webClient;

    @Autowired
    public StaffServiceImp(WebClient webClient) {
        this.webClient = webClient;
    }



    @Override
    public Mono<JwtResponse> loginStaff(JwtRequest request) {
        System.out.println("Inside loginStaff");
        return webClient.post()
                .uri("/branchlogin")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new RuntimeException("Login Failed: " + error)))
                )
                .bodyToMono(JwtResponse.class);
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
