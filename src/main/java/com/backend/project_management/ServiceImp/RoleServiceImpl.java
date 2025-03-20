package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.RoleDTO;
import com.backend.project_management.Entity.Role;
import com.backend.project_management.Exception.RequestNotFound;

import com.backend.project_management.Mapper.RoleMapper;
import com.backend.project_management.Repository.RoleRepository;
import com.backend.project_management.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = roleMapper.toEntity(roleDTO);
        Role savedRole = roleRepository.save(role);
        return roleMapper.toDTO(savedRole);
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public RoleDTO getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RequestNotFound("Role not found with id: " + id));
        return roleMapper.toDTO(role);
    }

    @Override
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RequestNotFound("Role not found with id: " + id));
        roleRepository.delete(role);
    }
}
