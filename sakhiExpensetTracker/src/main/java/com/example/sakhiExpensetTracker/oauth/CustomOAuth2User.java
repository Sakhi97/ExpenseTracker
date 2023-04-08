package com.example.sakhiExpensetTracker.oauth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.sakhiExpensetTracker.domain.AppUser;

public class CustomOAuth2User extends DefaultOAuth2User {

    private AppUser appUser;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey, AppUser appUser) {
        super(authorities, attributes, nameAttributeKey);
        this.appUser = appUser;
    }

    @Override
    public String getName() {
        return appUser.getUsername();
    }

    public String getEmail() {
        return appUser.getEmail();
    }

    public AppUser getAppUser() {
        return appUser;
    }
}


