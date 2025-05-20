package com.backend.project_management.Service;

import com.backend.project_management.Model.JwtRequest;
import com.backend.project_management.Model.JwtResponse;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface StaffService {
    public Mono<JwtResponse> loginStaff(JwtRequest request);
    public Map<String, Boolean> getPermissionsByEmail(String email);
    public Map<String, Object> getCrudPermissionForDepartmentByEmail(String email);

}
