package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.RoleDTO;
import com.backend.project_management.Entity.ProjectAdmin;
import com.backend.project_management.Entity.Role;
import com.backend.project_management.Entity.TeamLeader;
import com.backend.project_management.Exception.RequestNotFound;
import com.backend.project_management.Mapper.RoleMapper;
import com.backend.project_management.Repository.ProjectAdminRepo;
import com.backend.project_management.Repository.RoleRepository;
import com.backend.project_management.Repository.TeamLeaderRepository;
import com.backend.project_management.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private StaffValidation  staffValidation;

    @Autowired
    private ProjectAdminRepo adminRepo;

    @Autowired
    private TeamLeaderRepository teamLeaderRepository;



    @Override
    public RoleDTO createRole(RoleDTO roleDTO,String role,String email) {
        System.out.println("Checking permission for role: " + role + ", email: " + email);
        if (!staffValidation.hasPermission(role, email, "POST")) {
            System.out.println("Permission denied!");
            throw new AccessDeniedException("No permission to create Role");
        }
        System.out.println("Permission granted!");

        Role savedRole = roleMapper.toEntity(roleDTO);


        savedRole.setRole(role);
        savedRole.setCreatedByEmail(email);

        String branchCode = switch (role) {
            case "ADMIN" -> adminRepo.findByEmail(email)
                    .orElseThrow(() -> new RequestNotFound("Admin not found"))
                    .getBranchCode();
            case "TEAM_LEADER" -> teamLeaderRepository.findByEmail(email)
                    .orElseThrow(() -> new RequestNotFound("Team Leader not found"))
                    .getBranchCode();
            default -> staffValidation.fetchBranchCodeByRole(role, email);
        };

        savedRole.setBranchCode(branchCode);
        Role save = roleRepository.save(savedRole);
        System.out.println("Role saved with id: " + save.getId());

        return roleMapper.toDTO(save);
    }

    @Override
    public List<RoleDTO> getAllRoles(String role, String email, String branchCode) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to getAllRoles");
        }
        List<Role> roles = roleRepository.findAllByBranchCode(branchCode);
        return roles.stream().map(roleMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public RoleDTO getRoleById(Long id,String role,String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to getRoleById");
        }
        Role role1 = roleRepository.findById(id)
                .orElseThrow(() -> new RequestNotFound("Role not found with id: " + id));
        return roleMapper.toDTO(role1);
    }

    @Override
    public void deleteRole(Long id,String role,String email) {
        if (!staffValidation.hasPermission(role, email, "DELETE")) {
            throw new AccessDeniedException("You do not have permission to deleteRole");
        }
        Role role1 = roleRepository.findById(id)
                .orElseThrow(() -> new RequestNotFound("Role not found with id: " + id));
        roleRepository.delete(role1);
    }
}
