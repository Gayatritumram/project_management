package com.backend.project_management.Service;

import com.backend.project_management.DTO.TeamLeaderDTO;
import com.backend.project_management.Entity.TeamLeader;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TeamLeaderService {
    TeamLeader createTeamLeader(TeamLeaderDTO dto, MultipartFile imageFile,String role, String email) throws IOException;
    TeamLeaderDTO updateTeamLeader(Long id, TeamLeaderDTO teamLeaderDTO,String  role, String email);
    void deleteTeamLeader(Long id,String role, String email);
    TeamLeaderDTO getTeamLeaderById(Long id, String role, String email);
    List<TeamLeaderDTO> getAllTeamLeaders(String role, String email, String branchCode);
    TeamLeaderDTO getTeamLeaderByEmail(String email,String role);
    String forgotPassword(String email);
    String verifyOtp(String email, int otp);
    String resetPassword(String email, String newPassword, String confirmPassword);
}
