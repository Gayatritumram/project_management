package com.backend.project_management.Service;

import com.backend.project_management.DTO.TeamLeaderDTO;
import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Entity.TeamLeader;
import com.backend.project_management.Entity.TeamMember;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TeamMemberService {
    TeamMember createTeamMember(TeamMemberDTO dto, MultipartFile imageFile) throws IOException;

    TeamMemberDTO getTeamMemberById(Long id);

    List<TeamMemberDTO> getAllTeamMembers();

    //
    void makeTeamLeader(Long id);

    TeamMemberDTO updateTeamMember(Long id, TeamMemberDTO teamMemberDTO);

    TeamMemberDTO updateImageUrl(Long memberId, String imageUrl);

    void deleteTeamMember(Long id);

    String forgotPassword(String email);

    String verifyOtp(String email, int otp);

    String resetPassword(String email, String newPassword, String confirmPassword);


}
