package com.backend.project_management.Controller;

import com.backend.project_management.Model.JwtRequest;
import com.backend.project_management.Model.JwtResponse;
import com.backend.project_management.Service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class StaffController
{
    @Autowired
    private StaffService staffLoginService;

    @PostMapping("/stafflogin")
    public Mono<ResponseEntity<JwtResponse>> loginStaff(@RequestBody JwtRequest request) {
        return staffLoginService.loginStaff(request)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    JwtResponse errorResponse = new JwtResponse();
                    errorResponse.setToken(null);
                    errorResponse.setData(Map.of("error", "Login failed: " + e.getMessage()));
                    return Mono.just(ResponseEntity.status(500).body(errorResponse));
                });
    }

    @GetMapping("/permissionForStaff")
    public Map<String, Boolean> getPermission(@RequestParam String staffEmail) {
        return staffLoginService.getPermissionsByEmail(staffEmail);
    }


    @GetMapping("/permissionForDepartment")
    public ResponseEntity<Map<String, Object>> getDepartmentPermissions(@RequestParam String departmentEmail) {

        Map<String, Object> permissions = staffLoginService.getCrudPermissionForDepartmentByEmail(departmentEmail);
        return ResponseEntity.ok(permissions);
    }

}
