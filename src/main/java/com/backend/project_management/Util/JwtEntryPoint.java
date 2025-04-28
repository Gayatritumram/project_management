package com.backend.project_management.Util;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("text/plain");
        PrintWriter printWriter = response.getWriter();
        
        // Get the exception message that may have been set in the request attributes
        String errorMsg = (String) request.getAttribute("auth_error_message");
        String errorMessage;
        
        if (errorMsg != null) {
            errorMessage = errorMsg;
        } else if (authException.getMessage() != null && authException.getMessage().contains("Bad credentials")) {
            errorMessage = "Invalid password, Please enter valid password";
        } else {
            errorMessage = "Access Denied: Full authentication is required to access this resource";
        }
        
        printWriter.println(errorMessage);
    }
}
//authentication