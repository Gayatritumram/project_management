package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.BranchResponseDTO;
import com.backend.project_management.DTO.TeamLeaderDTO;
import com.backend.project_management.Entity.Branch;
import com.backend.project_management.Mapper.BranchMapper;
import com.backend.project_management.Repository.BranchRepository;
import com.backend.project_management.Service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class BranchServiceImpl implements BranchService {
    @Autowired
    private BranchRepository repository;

    @Autowired
    private BranchMapper mapper;

    @Autowired
    private StaffValidation  staffValidation;


    @Override
    public BranchResponseDTO CreateBranch(BranchResponseDTO branchDTO,String role,String email) {
        if (!staffValidation.hasPermission(role, email, "POST")) {
            System.out.println("Permission denied!");
            throw new AccessDeniedException("No permission to create Branch");
        }
        Branch branch = mapper.toEntity(branchDTO);

        branch.setCreatedByEmail(email);
        branch.setRole(role);
        String branchCode = staffValidation.fetchBranchCodeByRole(role, email);
        branch.setBranchCode(branchCode);
        branch.setCreatedDate(LocalDateTime.now());
        return mapper.toResponseDTO(repository.save(branch));
    }

    @Override
    public BranchResponseDTO UpdateBranch(Long id, BranchResponseDTO branchResponseDTO, String role, String email) {
            if (!staffValidation.hasPermission(role, email, "PUT")) {
                throw new AccessDeniedException("You do not have permission to update Branch");
            }
        Branch branch = repository.findById(id).orElseThrow(()-> new RuntimeException("Branch not found"));
        branch.setBranchName(branchResponseDTO.getBranchName());
        branch.setCreatedDate(LocalDateTime.now());
        return mapper.toResponseDTO(repository.save(branch));
    }

    @Override
    public BranchResponseDTO getBranchById(Long id, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view getBranchId");
        }
        Branch branch = repository.findById(id).orElseThrow(()->new RuntimeException("Branch not found"));
            return
                    mapper.toResponseDTO(branch);
        }

    @Override
    public List<BranchResponseDTO> getAllBranches(String role, String email,String branchCode) {
        if(!staffValidation.hasPermission(role, email, "GET")){
            throw new AccessDeniedException("You do not have permission to view Branch");
        }
            return repository.findAll()
                    .stream()
                    .map(mapper::toResponseDTO)
                    .collect(Collectors.toList());
    }
    @Override
    public void deleteBranch(Long id,String role, String email) {
        if (!staffValidation.hasPermission(role, email, "DELETE")) {
            throw new AccessDeniedException("You do not have permission to view Branch");
        }

        if (!repository.existsById(id)) {
            throw new RuntimeException("BRANCH not found with id: " + id);
        }
       repository.deleteById(id);
    }
}

