package com.backend.project_management.Controller;
import com.backend.project_management.Service.ProjectAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class ProjectAdminController {
    @Autowired
    private ProjectAdminService adminService;





}
