package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.DepartmentDTO;
import com.backend.project_management.Entity.Department;
import com.backend.project_management.Entity.Project;
import com.backend.project_management.Entity.Team;
import com.backend.project_management.Exception.RequestNotFound;
import com.backend.project_management.Mapper.DepartmentMapper;
import com.backend.project_management.Mapper.ProjectMapper;
import com.backend.project_management.Repository.DepartmentRepository;
import com.backend.project_management.Service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private StaffValidation staffValidation;

    @Override
    public DepartmentDTO createDepartment(DepartmentDTO dto, String email, String role) {

        if (!staffValidation.hasPermission(role, email, "POST")) {
            System.out.println("Permission denied!");
            throw new AccessDeniedException("No permission to create project");
        }
        System.out.println("Permission granted!");

        Department department = departmentMapper.toEntity(dto);

        String branchCode = staffValidation.fetchBranchCodeByRole(role, email);
        department.setRole(role);
        department.setCreatedByEmail(email);
        department.setBranchCode(branchCode);

        LocalDateTime now = LocalDateTime.now();
        department.setCreatedDate(now);

        Department savedTask = departmentRepository.save(department);

        return departmentMapper.toDTO(savedTask);
    }

    @Override
    public DepartmentDTO updateDepartment(Long id, DepartmentDTO dto, String email, String role){
        if (!staffValidation.hasPermission(role, email, "PUT")) {
            throw new AccessDeniedException("You do not have permission to make department");
        }
        Department department = departmentRepository.findById(id).orElseThrow(() -> new RequestNotFound("Department not found"));
        department.setDepartmentName(dto.getDepartmentName());
        LocalDateTime now = LocalDateTime.now();
        department.setCreatedDate(now);

        Department updatedDepartment = departmentRepository.save(department);
        return departmentMapper.toDTO(updatedDepartment);
    }

    @Override
    public DepartmentDTO getDepartmentById(Long id, String email, String role){
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view department");
        }
        Department department = departmentRepository.findById(id).orElseThrow(() -> new RequestNotFound("Department not found"));
        return departmentMapper.toDTO(department);
    }

    @Override
    public  List<DepartmentDTO> getAllDepartments(String role,String email, String branchCode) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view getAllDepartments");
        }
        List<Department> departments = departmentRepository.findAllByBranchCode(branchCode);
        if (!departments.isEmpty()) {
            return departments.stream().map(departmentMapper::toDTO).collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public void deleteDepartment(Long id, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "DELETE")) {
            throw new AccessDeniedException("You do not have permission to delete department");
        }
        departmentRepository.deleteById(id);
    }
}