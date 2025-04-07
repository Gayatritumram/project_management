package com.backend.project_management.Service;

public interface EmailService {
    void sendEMail(String to, String subject, String body);
}
