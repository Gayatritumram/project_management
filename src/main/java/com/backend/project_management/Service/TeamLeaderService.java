package com.backend.project_management.Service;

import com.backend.project_management.DTO.ProjectDTO;
import com.backend.project_management.DTO.TeamLeaderDTO;
import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Entity.TeamLeader;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TeamLeaderService {
    TeamLeaderDTO createTeamLeader(TeamLeaderDTO dto, MultipartFile imageFile,String role, String email) throws IOException;
    TeamLeaderDTO updateTeamLeader(Long id, TeamLeaderDTO teamLeaderDTO,String  role, String email);
    void deleteTeamLeader(Long id,String role, String email);
    TeamLeaderDTO getTeamLeaderById(Long id, String role, String email);

    List<TeamLeaderDTO> getAllTeamLeaders(String role, String email, String branchCode);
    TeamLeaderDTO getTeamLeaderByEmail(String email,String role,String emailFind);
    String forgotPassword(String email);
    String verifyOtp(String email, int otp);
    String resetPassword(String email, String newPassword, String confirmPassword);

    void updateTeamMemberProfilePicture(Long leaderId, MultipartFile imageFile, String role, String email);
    TeamLeaderDTO getTeamLeaderByName(String name, String role, String email);


    public Page<TeamLeaderDTO> getAllTeamLeaders(String role, String email, String branchCode,
                                                 String searchBy, int page, int size,
                                                 String sortBy, String sortDir);

    List<TeamMemberDTO> getAllTeamMemberByLeaderId(Long id,String role,String email);

    List<ProjectDTO> getProjectsByTeamLeadersId(Long id, String role, String email);

}
