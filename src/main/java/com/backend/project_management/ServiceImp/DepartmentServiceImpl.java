package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.DepartmentDTO;
import com.backend.project_management.Entity.Department;

import com.backend.project_management.Exception.RequestNotFound;
import com.backend.project_management.Mapper.DepartmentMapper;
import com.backend.project_management.Repository.DepartmentRepository;
import com.backend.project_management.Repository.ProjectAdminRepo;
import com.backend.project_management.Repository.TeamLeaderRepository;
import com.backend.project_management.Service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private StaffValidation  staffValidation;

    @Autowired
    private ProjectAdminRepo adminRepo;

    @Autowired
    private TeamLeaderRepository teamLeaderRepository;


    @Override
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO, String role, String email) {
        System.out.println("Checking permission for role: " + role + ", email: " + email);
        if (!staffValidation.hasPermission(role, email, "POST")) {
            System.out.println("Permission denied!");
            throw new AccessDeniedException("No permission to create Department");
        }
        System.out.println("Permission granted!");

        Department department = departmentMapper.toEntity(departmentDTO);

        String branchCode = switch (role) {
            case "ADMIN" -> adminRepo.findByEmail(email)
                    .orElseThrow(() -> new RequestNotFound("Admin not found"))
                    .getBranchCode();
            case "TEAM_LEADER" -> teamLeaderRepository.findByEmail(email)
                    .orElseThrow(() -> new RequestNotFound("Team Leader not found"))
                    .getBranchCode();
            default -> staffValidation.fetchBranchCodeByRole(role, email);
        };

        department.setRole(role);
        department.setCreatedByEmail(email);
        department.setBranchCode(branchCode);

        Department savedDepartment = departmentRepository.save(department);
        System.out.println("Department saved with id: " + savedDepartment.getId());

        return departmentMapper.toDTO(savedDepartment);
    }


    @Override
    public List<DepartmentDTO> getAllDepartments(String role,String email, String branchCode) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view class rooms");
        }

        List<Department> departments = departmentRepository.findAllByBranchCode(branchCode);
        return departments.stream().map(departmentMapper::toDTO).collect(Collectors.toList());
    }


    @Override
    public DepartmentDTO getDepartmentById(Long id, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view class rooms");
        }

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RequestNotFound("Department not found with id: " + id));
        return departmentMapper.toDTO(department);
    }

    @Override
    public void deleteDepartment(Long id,String role, String email) {
        if (!staffValidation.hasPermission(role, email, "DELETE")) {
            throw new AccessDeniedException("You do not have permission to view class rooms");
        }

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RequestNotFound("Department not found with id: " + id));
        departmentRepository.delete(department);
    }
}
