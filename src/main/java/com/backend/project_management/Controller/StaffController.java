package com.backend.project_management.Controller;

import com.backend.project_management.Entity.BranchAdmin;
import com.backend.project_management.Model.JwtRequest;
import com.backend.project_management.Model.JwtResponse;
import com.backend.project_management.Service.BranchAdminService;
import com.backend.project_management.Service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@CrossOrigin(origins = "https://pjsofttech.in")
public class StaffController
{
    @Autowired
    private StaffService staffLoginService;



    @PostMapping("/loginBranch")
    public Mono<ResponseEntity<JwtResponse>> loginBranch(@RequestBody JwtRequest request) {

        return staffLoginService.loginStaff(request)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    JwtResponse errorResponse = new JwtResponse();
                    errorResponse.setToken(null); // explicitly null
                    errorResponse.setData(Map.of("error", "Login failed: " + e.getMessage()));

                    // If it's a known invalid credentials issue, return 401
                    if (e.getMessage().toLowerCase().contains("invalid") || e.getMessage().toLowerCase().contains("unauthorized")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse));
                    }

                    // Otherwise, internal server error

                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
                });

    }

    @GetMapping("/getPermission")
    public Map<String, Boolean> getPermission(@RequestParam String staffEmail) {
        return staffLoginService.getPermissionsByEmail(staffEmail);
    }


    @GetMapping("/permissionForDepartment")
    public ResponseEntity<Map<String, Object>> getDepartmentPermissions(@RequestParam String departmentEmail) {

        Map<String, Object> permissions = staffLoginService.getCrudPermissionForDepartmentByEmail(departmentEmail);
        return ResponseEntity.ok(permissions);
    }

}
