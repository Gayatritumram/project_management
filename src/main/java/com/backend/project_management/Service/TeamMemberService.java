package com.backend.project_management.Service;

import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Entity.TeamMember;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TeamMemberService {
    TeamMemberDTO createTeamMember(TeamMemberDTO dto, MultipartFile imageFile , String role, String email) throws IOException;
    TeamMemberDTO getTeamMemberById(Long id,String role,String  email);
    List<TeamMemberDTO> getAllTeamMembers(String role,String  email,String  branchCode);

    TeamMemberDTO getTeamMemberByEmail(String memberEmail, String role, String email);

    void makeTeamLeader(Long id, String role,String  email);
    TeamMemberDTO updateTeamMember(Long id, TeamMemberDTO teamMemberDTO,String role,String  email);
    void deleteTeamMember(Long id,String role,String  email);


    String forgotPassword(String email);

    String verifyOtp(String email, int otp);

    String resetPassword(String email, String newPassword, String confirmPassword);

    void updateTeamMemberProfilePicture(Long memberId,MultipartFile imageFile, String role, String email);
    TeamMemberDTO getTeamMemberByName(String name, String role, String email);

    List<TeamMemberDTO> filterTeamMembers(String name, String branchName, String departmentName, String roleName, String role, String email);









}
