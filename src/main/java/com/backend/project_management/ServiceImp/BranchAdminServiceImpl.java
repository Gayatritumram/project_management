package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.BranchData;
import com.backend.project_management.DTO.SystemDTO;
import com.backend.project_management.Entity.BranchAdmin;
import com.backend.project_management.Model.JwtResponse;
import com.backend.project_management.Repository.BranchAdminRepository;
import com.backend.project_management.Service.BranchAdminService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BranchAdminServiceImpl implements BranchAdminService {

     @Autowired
     private BranchAdminRepository branchAdminRepository;

     @Autowired
     @Lazy // Helps break circular dependency
     private StaffValidation staffValidation;

     @Override
     public void saveBranchAdmin(JwtResponse jwtResponse) {
          ObjectMapper mapper = new ObjectMapper();
          BranchData branchData = mapper.convertValue(jwtResponse.getData(), BranchData.class);

          Optional<BranchAdmin> existing = branchAdminRepository.findByBranchEmail(branchData.getBranchEmail());

          if (existing.isPresent()) {
               BranchAdmin existingAdmin = existing.get();
               existingAdmin.setLoginDateTime(LocalDateTime.now());
               branchAdminRepository.save(existingAdmin); // Optional: update login time
               System.out.println("Branch admin already exists. Updated login time.");
               return;
          }

          BranchAdmin admin = new BranchAdmin();
          admin.setBid(branchData.getBid());
          admin.setEmail(branchData.getBranchEmail());
          admin.setBranchCode(branchData.getBranchCode());
          admin.setBranchName(branchData.getBranchName());
          admin.setBranchEmail(branchData.getBranchEmail());
          admin.setInstituteEmail(branchData.getInstituteEmail());
          admin.setLoginDateTime(LocalDateTime.now());

          List<SystemDTO> systems = branchData.getSystems();
          String systemNames = (systems != null && !systems.isEmpty())
                  ? systems.stream()
                  .map(SystemDTO::getName)
                  .filter(Objects::nonNull)
                  .collect(Collectors.joining(","))
                  : "Unknown";

          admin.setSystems(systemNames);

          branchAdminRepository.save(admin);
     }

     @Override
     public List<BranchAdmin> getAllBranchAdmins(String role, String email, String branchCode) {
          checkPermission(role, email);
          return branchAdminRepository.findAll();
     }

     @Override
     public BranchAdmin getByBranchCode(String branchCode, String role, String email) {
          checkPermission(role, email);
          return branchAdminRepository.findByBranchCode(branchCode)
                  .orElseThrow(() -> new RuntimeException("Branch Admin not found with code: " + branchCode));
     }

     @Override
     public BranchAdmin getByBranchEmail(String emailFind, String role, String email) {
          checkPermission(role, email);
          return branchAdminRepository.findByBranchEmail(emailFind)
                  .orElseThrow(() -> new RuntimeException("Branch Admin not found with email: " + emailFind));
     }

     private void checkPermission(String role, String email) {
          if (!staffValidation.hasPermission(role, email, "GET")) {
               throw new AccessDeniedException("Permission denied for action: " + "GET");
          }
     }
}
