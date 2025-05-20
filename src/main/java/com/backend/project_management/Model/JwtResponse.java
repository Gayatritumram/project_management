package com.backend.project_management.Model;

import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    //private String jwtToken;
    private String token;

    //private String username;
    //private String role;
    //private Long id;
    private Map<String, Object> data;

}
