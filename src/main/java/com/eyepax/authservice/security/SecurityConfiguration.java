package com.eyepax.authservice.security;

import com.eyepax.authservice.repository.UserRepository;
import com.eyepax.authservice.service.AuditLogService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Class to configure AWS Cognito as an OAuth 2.0 authorizer with Spring Security.
 * In this configuration, we specify our OAuth Client.
 * We also declare that all requests must come from an authenticated user.
 * Finally, we configure our logout handler.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuditLogService auditLogService,
                                           UserRepository userRepository) throws Exception {
        CognitoLogoutHandler cognitoLogoutHandler = new CognitoLogoutHandler(auditLogService, userRepository);

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler((request, response, auth) -> {
                            if (auth != null && auth.getName() != null) {
                                System.out.println(">>> Logout handler called for: " + auth.getName());
                            }
                        })
                        .logoutSuccessHandler(cognitoLogoutHandler)
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                );

        return http.build();
    }

}
