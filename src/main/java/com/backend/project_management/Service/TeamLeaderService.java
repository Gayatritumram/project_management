package com.backend.project_management.Service;

import com.backend.project_management.DTO.TeamLeaderDTO;
import java.util.List;

public interface TeamLeaderService {
    TeamLeaderDTO createTeamLeader(TeamLeaderDTO teamLeaderDTO);
    TeamLeaderDTO updateTeamLeader(Long id, TeamLeaderDTO teamLeaderDTO);
    void deleteTeamLeader(Long id);
    TeamLeaderDTO getTeamLeaderById(Long id);
    List<TeamLeaderDTO> getAllTeamLeaders();
    TeamLeaderDTO getTeamLeaderByEmail(String email);
    String forgotPassword(String email);
    String verifyOtp(String email, int otp);
    String resetPassword(String email, String newPassword, String confirmPassword);
}
