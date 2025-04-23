package com.backend.project_management.Service;

import com.backend.project_management.DTO.TeamMemberDTO;

import java.util.List;

public interface TeamMemberService {
    TeamMemberDTO createTeamMember(TeamMemberDTO teamMemberDTO);
    TeamMemberDTO getTeamMemberById(Long id);
    List<TeamMemberDTO> getAllTeamMembers();
//
//     void makeTeamLeader(Long id);
  TeamMemberDTO updateTeamMember(Long id, TeamMemberDTO teamMemberDTO);
    void deleteTeamMember(Long id);
    String forgotPassword(String email);

    String verifyOtp(String email, int otp);

    String resetPassword(String email, String newPassword, String confirmPassword);
}
