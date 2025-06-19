package com.backend.project_management.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class   LoginDTO {

        private String email;
        private String password;

        // Getters and setters


}
