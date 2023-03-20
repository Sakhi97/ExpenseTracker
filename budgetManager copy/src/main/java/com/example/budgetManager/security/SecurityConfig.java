package com.example.budgetManager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity


public class SecurityConfig {
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
        
        .authorizeRequests()
        	.requestMatchers("/css/**","/js/**").permitAll() // Enable css when logged out
        .and()
        .authorizeRequests().anyRequest().authenticated()
        .and()
      .formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/budgets", true)
				.permitAll().and()
      .logout()
          .logoutSuccessUrl("/")
          .permitAll()
          .and()
      .httpBasic();
        
        return http.build();
	 }

	@Autowired public void configureGlobal (AuthenticationManagerBuilder auth) throws Exception { 
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder()); 
		}
	}

    




