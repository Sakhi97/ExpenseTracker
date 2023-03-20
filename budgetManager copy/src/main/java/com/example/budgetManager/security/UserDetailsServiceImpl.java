package com.example.budgetManager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.budgetManager.domain.AppUser;
import com.example.budgetManager.repositories.AppUserRepository;


	@Service
	public class UserDetailsServiceImpl implements UserDetailsService  {

	@Autowired
	private AppUserRepository userRepo;
	    @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {   
	    	
	    	AppUser appuser = userRepo.findByUsername(username);
	    	
	    	if (appuser == null)
	    		throw new UsernameNotFoundException("User does not exist");
	    	
	        return new SecurityUser(appuser);
	    }   
	} 
	

