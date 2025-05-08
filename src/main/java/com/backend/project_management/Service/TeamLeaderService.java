package com.backend.project_management.Service;

import com.backend.project_management.DTO.TeamLeaderDTO;
import com.backend.project_management.Entity.TeamLeader;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TeamLeaderService {
    public TeamLeader createTeamLeader(TeamLeaderDTO dto, MultipartFile imageFile) throws IOException;
    TeamLeaderDTO updateTeamLeader(Long id, TeamLeaderDTO teamLeaderDTO);
    void deleteTeamLeader(Long id);
    TeamLeaderDTO getTeamLeaderById(Long id);
    List<TeamLeaderDTO> getAllTeamLeaders();
    TeamLeaderDTO getTeamLeaderByEmail(String email);
    String forgotPassword(String email);
    String verifyOtp(String email, int otp);
    String resetPassword(String email, String newPassword, String confirmPassword);
}
