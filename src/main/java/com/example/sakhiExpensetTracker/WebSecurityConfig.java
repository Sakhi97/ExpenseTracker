package com.example.sakhiExpensetTracker;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.example.sakhiExpensetTracker.oauth.CustomOAuth2UserService;
import com.example.sakhiExpensetTracker.oauth.OAuth2LoginSuccessHandler;
import com.example.sakhiExpensetTracker.web.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler() {
        return new OAuth2LoginSuccessHandler();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        OAuth2LoginSuccessHandler successHandler = oAuth2LoginSuccessHandler();

        http
            .authorizeRequests()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/oauth2/**").permitAll()
                .requestMatchers("/signup", "/saveuser").permitAll()
                .requestMatchers("/login/oauth2/code/*").permitAll() // allow OAuth2 redirect URI
            .and()
            .authorizeRequests().anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/expenselist", true)
                .permitAll()
            .and()
            .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint().userService(customOAuth2UserService)
                .and()
                .successHandler(successHandler)
            .and()
            .logout()
                .permitAll()
            .and()
            .httpBasic();
        
        return http.build();
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
}
