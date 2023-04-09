package com.example.sakhiExpensetTracker.oauth;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    // Define a logger for this class
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2LoginSuccessHandler.class);

    // This method is called when the authentication is successful
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        LOGGER.info("OAuth2LoginSuccessHandler - onAuthenticationSuccess triggered");

        // Get the authenticated user's details
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("Attributes: " + oAuth2User.getAttributes());

        // Log the success and redirect to the expense list page
        LOGGER.info("OAuth2LoginSuccessHandler - Redirecting to /expenselist");
        response.sendRedirect("/expenselist");
    }

}









