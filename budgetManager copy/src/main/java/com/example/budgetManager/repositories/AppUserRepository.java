package com.example.budgetManager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.budgetManager.domain.AppUser;

	public interface AppUserRepository extends JpaRepository<AppUser, Long> {
		AppUser findByUsername(String username);
	}


