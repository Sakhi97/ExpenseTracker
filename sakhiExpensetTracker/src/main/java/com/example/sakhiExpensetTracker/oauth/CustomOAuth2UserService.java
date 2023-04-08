package com.example.sakhiExpensetTracker.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.sakhiExpensetTracker.domain.AppUser;
import com.example.sakhiExpensetTracker.domain.AppUserRepository;
import com.example.sakhiExpensetTracker.domain.AuthenticationProvider;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = null;

        try {
            oAuth2User = super.loadUser(userRequest);
            System.out.println("Attributes: " + oAuth2User.getAttributes());
        } catch (OAuth2AuthenticationException e) {
            System.err.println("OAuth2AuthenticationException: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        String email = (String) oAuth2User.getAttributes().get("email");

        if (email == null) {
            email = fetchUserEmail(userRequest.getAccessToken().getTokenValue());
            if (email == null) {
                throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
            }
        }

        AppUser appUser = userRepository.findByEmail(email);

        if (appUser == null) {
            // Register the new user
            appUser = new AppUser();
            appUser.setEmail(email);
            appUser.setUsername(email); // Set the username to the email
            appUser.setFirstName(oAuth2User.getAttribute("given_name") != null ? oAuth2User.getAttribute("given_name") : "Unknown");
            appUser.setLastName(oAuth2User.getAttribute("family_name") != null ? oAuth2User.getAttribute("family_name") : "Unknown");
            appUser.setAuthProvider(AuthenticationProvider.GITHUB);
            appUser.setRole("USER");
            appUser.setBudget(0.0);
            appUser.setPasswordHash("");// Set the password field to null
            appUser = userRepository.save(appUser);
        }

        return new CustomOAuth2User(oAuth2User.getAuthorities(),
                oAuth2User.getAttributes(),
                "email",
                appUser);
    }

    private String fetchUserEmail(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        JsonNode response = restTemplate.exchange("https://api.github.com/user/emails", HttpMethod.GET, entity, JsonNode.class).getBody();

        if (response != null && response.isArray()) {
            for (JsonNode emailNode : response) {
                if (emailNode.has("primary") && emailNode.get("primary").asBoolean()) {
                    return emailNode.get("email").asText();
                }
            }
        }

        return null;
    }
}
