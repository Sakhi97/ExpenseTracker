package com.example.sakhiExpensetTracker.oauth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.sakhiExpensetTracker.domain.AppUser;
import com.example.sakhiExpensetTracker.domain.AppUserRepository;
import com.example.sakhiExpensetTracker.domain.AuthenticationProvider;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2LoginSuccessHandler.class);

    @Autowired
    private AppUserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        LOGGER.info("OAuth2LoginSuccessHandler - onAuthenticationSuccess triggered");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("Attributes: " + oAuth2User.getAttributes());

        LOGGER.info("OAuth2LoginSuccessHandler - Redirecting to /expencelist");
        response.sendRedirect("/expencelist");
    }

}





