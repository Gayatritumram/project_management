package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.BranchDTO;
import com.backend.project_management.Entity.Branch;
import com.backend.project_management.Exception.BranchNotFoundException;
import com.backend.project_management.Mapper.BranchMapper;
import com.backend.project_management.Repository.BranchRepository;
import com.backend.project_management.Service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BranchServiceImpl implements BranchService {
    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BranchMapper branchMapper;

    @Override
    public BranchDTO createBranch(BranchDTO branchDTO) {
        Branch branch = branchMapper.toEntity(branchDTO);
        Branch savedBranch = branchRepository.save(branch);
        return branchMapper.toDTO(savedBranch);
    }

    @Override
    public List<BranchDTO> getAllBranches() {
        List<Branch> branches = branchRepository.findAll();
        return branches.stream().map(branchMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public BranchDTO getBranchById(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found with id: " + id));
        return branchMapper.toDTO(branch);
    }

    @Override
    public void deleteBranch(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found with id: " + id));
        branchRepository.delete(branch);
    }
}
