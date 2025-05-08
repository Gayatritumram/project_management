package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.TeamLeaderDTO;
import com.backend.project_management.Entity.Team;
import com.backend.project_management.Entity.TeamLeader;
import com.backend.project_management.Mapper.TeamLeaderMapper;
import com.backend.project_management.Repository.TeamLeaderRepository;
import com.backend.project_management.Repository.TeamRepository;
import com.backend.project_management.Service.EmailService;
import com.backend.project_management.Service.OtpService;
import com.backend.project_management.Service.TeamLeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamLeaderServiceImpl implements TeamLeaderService {

    @Autowired
    private TeamLeaderRepository teamLeaderRepository;

    @Autowired
    private TeamLeaderMapper teamLeaderMapper;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired private S3Service s3Service;





    @Override
    public TeamLeader createTeamLeader(TeamLeaderDTO dto, MultipartFile imageFile) throws IOException {
        TeamLeader leader = new TeamLeader();
        leader.setName(dto.getName());
        leader.setEmail(dto.getEmail());
        leader.setPassword(passwordEncoder.encode(dto.getPassword())); // Hash if needed
        leader.setPhone(dto.getPhone());
        leader.setAddress(dto.getAddress());
        leader.setDepartment(dto.getDepartment());
        leader.setBranchName(dto.getBranchName());
        leader.setJoinDate(dto.getJoinDate());


        if (dto.getTeamId() != null) {
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found"));
            leader.setTeamId(team);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = s3Service.uploadImage(imageFile); // S3 or local path
            leader.setImageUrl(imageUrl);
        }

        return teamLeaderRepository.save(leader);
    }


    @Override
    public TeamLeaderDTO updateTeamLeader(Long id, TeamLeaderDTO dto) {
        TeamLeader leader = teamLeaderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TeamLeader not found with id: " + id));

        leader.setName(dto.getName());
        leader.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            leader.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        leader.setPhone(dto.getPhone());
        leader.setAddress(dto.getAddress());
        leader.setDepartment(dto.getDepartment());
        leader.setBranchName(dto.getBranchName());

        TeamLeader updated = teamLeaderRepository.save(leader);
        return teamLeaderMapper.toDto(updated);
    }

    @Override
    public void deleteTeamLeader(Long id) {
        if (!teamLeaderRepository.existsById(id)) {
            throw new RuntimeException("TeamLeader not found with id: " + id);
        }
        teamLeaderRepository.deleteById(id);
    }

    @Override
    public TeamLeaderDTO getTeamLeaderById(Long id) {
        return teamLeaderRepository.findById(id)
                .map(teamLeaderMapper::toDto)
                .orElseThrow(() -> new RuntimeException("TeamLeader not found with id: " + id));
    }

    @Override
    public List<TeamLeaderDTO> getAllTeamLeaders() {
        return teamLeaderRepository.findAll()
                .stream()
                .map(teamLeaderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TeamLeaderDTO getTeamLeaderByEmail(String email) {
        return teamLeaderRepository.findByEmail(email)
                .map(teamLeaderMapper::toDto)
                .orElseThrow(() -> new RuntimeException("TeamLeader not found with email: " + email));
    }

    @Override
    public String forgotPassword(String email) {
        Optional<TeamLeader> optionalTeamLeader = teamLeaderRepository.findByEmail(email);
        if (optionalTeamLeader.isPresent()) {
            boolean otpSent = otpService.generateAndSendOTP(email);
            if (otpSent) {
                return "OTP has been sent to your email: " + email;
            } else {
                throw new IllegalArgumentException("Error in sending OTP. Please try again.");
            }
        } else {
            throw new IllegalArgumentException("Team Leader email not found!");
        }
    }

    @Override
    public String verifyOtp(String email, int otp) {
        if (otpService.verifyOTP(email, otp)) {
            return "OTP is valid. You can now reset your password.";
        }
        throw new IllegalArgumentException("Invalid or expired OTP!");
    }

    @Override
    public String resetPassword(String email, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match!");
        }

        Optional<TeamLeader> optionalLeader = teamLeaderRepository.findByEmail(email);
        if (optionalLeader.isPresent()) {
            TeamLeader teamLeader = optionalLeader.get();
            teamLeader.setPassword(passwordEncoder.encode(newPassword));
            teamLeaderRepository.save(teamLeader);
            return "Password successfully reset.";
        } else {
            throw new IllegalArgumentException("Team Leader email not found!");
        }
    }
}
