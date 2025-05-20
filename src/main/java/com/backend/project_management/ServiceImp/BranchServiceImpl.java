package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.BranchDTO;
import com.backend.project_management.Entity.Branch;
import com.backend.project_management.Exception.RequestNotFound;
import com.backend.project_management.Mapper.BranchMapper;
import com.backend.project_management.Repository.BranchRepository;
import com.backend.project_management.Repository.ProjectAdminRepo;
import com.backend.project_management.Repository.TeamLeaderRepository;
import com.backend.project_management.Service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BranchServiceImpl implements BranchService {
    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BranchMapper branchMapper;

    @Autowired
    private StaffValidation  staffValidation;

    @Autowired
    private ProjectAdminRepo adminRepo;

    @Autowired
    private TeamLeaderRepository teamLeaderRepository;


    @Override
    public BranchDTO createBranch(BranchDTO branchDTO,String role,String email) {
        System.out.println("Checking permission for role: " + role + ", email: " + email);
        if (!staffValidation.hasPermission(role, email, "POST")) {
            System.out.println("Permission denied!");
            throw new AccessDeniedException("No permission to create Branch");
        }
        System.out.println("Permission granted!");

        Branch branch = branchMapper.toEntity(branchDTO);


        String branchCode = switch (role) {
            case "ADMIN" -> adminRepo.findByEmail(email)
                    .orElseThrow(() -> new RequestNotFound("Admin not found"))
                    .getBranchCode();
            case "TEAM_LEADER" -> teamLeaderRepository.findByEmail(email)
                    .orElseThrow(() -> new RequestNotFound("Team Leader not found"))
                    .getBranchCode();
            default -> staffValidation.fetchBranchCodeByRole(role, email);
        };


        branch.setRole(role);
        branch.setCreatedByEmail(email);
        branch.setBranchCode(branchCode);

        Branch savedBranch = branchRepository.save(branch);
        System.out.println("Branch saved with id: " + savedBranch.getId());


        return branchMapper.toDTO(savedBranch);
    }


    @Override
    public List<BranchDTO> getAllBranches(String role, String email,String branchCode) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view Branch");
        }

        List<Branch> branches = branchRepository.findAllByBranchCode(branchCode);
        return branches.stream().map(branchMapper::toDTO).collect(Collectors.toList());
    }



    @Override
    public BranchDTO getBranchById(Long id,String role,String gmail) {
        if (!staffValidation.hasPermission(role, gmail, "GET")) {
            throw new AccessDeniedException("You do not have permission to view Branch");
        }
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RequestNotFound("Branch not found with id: " + id));
        return branchMapper.toDTO(branch);
    }



    @Override
    public void deleteBranch(Long id,String role,String email) {
        if (!staffValidation.hasPermission(role, email, "DELETE")) {
            throw new AccessDeniedException("You do not have permission to view Branch");
        }
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RequestNotFound("Branch not found with id: " + id));
        branchRepository.delete(branch);
    }

}
