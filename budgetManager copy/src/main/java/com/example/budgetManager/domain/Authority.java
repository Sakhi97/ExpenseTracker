package com.example.budgetManager.domain;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Authority implements GrantedAuthority {
	

	private static final long serialVersionUID = 1747690928892092930L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String authority;

	@ManyToOne
	private AppUser appuser;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public AppUser getAppUser() {
		return appuser;
	}

	public void setUser(AppUser appuser) {
		this.appuser = appuser;
	}
}
