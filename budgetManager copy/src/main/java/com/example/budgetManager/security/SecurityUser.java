package com.example.budgetManager.security;


	
	import java.util.Set;

	import org.springframework.security.core.userdetails.UserDetails;

import com.example.budgetManager.domain.AppUser;
import com.example.budgetManager.domain.Authority;



	public class SecurityUser extends AppUser implements UserDetails
	{
	  private static final long serialVersionUID = -3188700847853328L;



	  public SecurityUser(AppUser appuser)
	  {
	    this.setAuthorities(appuser.getAuthorities());
	    this.setBudgets(appuser.getBudgets());
	    this.setId(appuser.getId());
	    this.setPassword(appuser.getPassword());
	    this.setUsername(appuser.getUsername());
	  }

	  @Override
	  public Set<Authority> getAuthorities()
	  {
	    return super.getAuthorities();
	  }

	  @Override
	  public String getPassword()
	  {
	    return super.getPassword();
	  }

	  @Override
	  public String getUsername()
	  {
	    return super.getUsername();
	  }

	  @Override
	  public boolean isAccountNonExpired()
	  {
	    return true;
	  }

	  @Override
	  public boolean isAccountNonLocked()
	  {
	    return true;
	  }

	  @Override
	  public boolean isCredentialsNonExpired()
	  {
	    return true;
	  }

	  @Override
	  public boolean isEnabled()
	  {
	    return true;
	  }

	}



