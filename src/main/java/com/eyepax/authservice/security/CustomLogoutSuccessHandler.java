package com.eyepax.authservice.security;

import com.eyepax.authservice.repository.UserRepository;
import com.eyepax.authservice.service.AuditLogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    public CustomLogoutSuccessHandler(UserRepository userRepository, AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Long userId = null;
        if (authentication != null && authentication.getName() != null) {
            // try to resolve user by email or principal
            String principalName = authentication.getName();
            Optional.of(principalName)
                    .flatMap(userRepository::findByEmail)
                    .ifPresent(u -> {
                        auditLogService.record(u.getId(), "LOGOUT", "User logged out", request);
                    });
        }
        // redirect to home or login
        response.sendRedirect("/");
    }
}

