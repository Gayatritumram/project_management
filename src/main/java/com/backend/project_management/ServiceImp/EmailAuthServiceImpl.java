package com.backend.project_management.ServiceImp;


import com.backend.project_management.Service.EmailAuthService;
import com.backend.project_management.Util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailAuthServiceImpl implements EmailAuthService {

    @Autowired
    private JavaMailSender mailSender;


    @Override
    public String generatePasswordResetToken(String userId) {
        return TokenUtil.generateToken(userId);
    }

    public void sendPasswordResetEmail(String email, String token) {
        String resetLink = "http://localhost:8080/reset-password?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("Click the link below to reset your password:\n" + resetLink);
        mailSender.send(message);
    }
}

