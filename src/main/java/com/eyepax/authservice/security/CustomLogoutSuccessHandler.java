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

    // ✅ Frontend URL to redirect after logout
    private static final String FRONTEND_LOGIN_URL = "http://localhost:3000/";

    public CustomLogoutSuccessHandler(UserRepository userRepository, AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication)
            throws IOException, ServletException {

        if (authentication != null && authentication.getName() != null) {
            String principalName = authentication.getName();

            // Try resolving by email or Cognito sub
            Optional.of(principalName)
                    .flatMap(userRepository::findByEmail)
                    .or(() -> userRepository.findByCognitoSub(principalName))
                    .ifPresent(u -> {
                        auditLogService.record(u.getId(), "LOGOUT", "User logged out successfully", request);
                        System.out.println(">>> Logout audit recorded for: " + u.getEmail());
                    });
        }

        // ✅ Redirect to React login page
        response.sendRedirect(FRONTEND_LOGIN_URL);
    }
}

