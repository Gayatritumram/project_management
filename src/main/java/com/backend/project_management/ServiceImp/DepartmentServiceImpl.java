package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.DepartmentDTO;
import com.backend.project_management.Entity.Department;

import com.backend.project_management.Exception.RequestNotFound;
import com.backend.project_management.Mapper.DepartmentMapper;
import com.backend.project_management.Repository.DepartmentRepository;
import com.backend.project_management.Service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        Department department = departmentMapper.toEntity(departmentDTO);
        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toDTO(savedDepartment);
    }

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream().map(departmentMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public DepartmentDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RequestNotFound("Department not found with id: " + id));
        return departmentMapper.toDTO(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RequestNotFound("Department not found with id: " + id));
        departmentRepository.delete(department);
    }
}
