package com.eyepax.authservice.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

public class CognitoLogoutHandler extends SimpleUrlLogoutSuccessHandler {

    /**
     * The domain of your user pool.
     */
    private String domain = "https://ap-southeast-2jtbvli75u.auth.ap-southeast-2.amazoncognito.com";

    /**
     * An allowed callback URL.
     */
    private String logoutRedirectUrl = "http://localhost:8080";

    /**
     * The ID of your User Pool Client.
     */
    private String userPoolClientId = "2vkd66pts7u65lmjoodml0b3nc";

    /**
     * Here, we must implement the new logout URL request. We define what URL to send our request to, and set out client_id and logout_uri parameters.
     */
    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        return UriComponentsBuilder
                .fromUri(URI.create(domain + "/logout"))
                .queryParam("client_id", userPoolClientId)
                .queryParam("logout_uri", logoutRedirectUrl)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUriString();
    }
}
