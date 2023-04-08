package com.example.sakhiExpensetTracker;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

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
                .requestMatchers("/login/oauth2/code/*").permitAll()
            .and()
            .authorizeRequests().anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/expencelist", true)
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

	

/*
spring.datasource.url = jdbc:mysql://localhost:3306/bookStore
spring.datasource.driverClassName = com.mysql.cj.jdbc.Driver
spring.datasource.username=bookStore
spring.datasource.password=12345678

spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update

spring.thymeleaf.cache=false
*/