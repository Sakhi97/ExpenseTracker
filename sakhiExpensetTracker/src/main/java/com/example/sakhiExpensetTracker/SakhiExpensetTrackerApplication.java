package com.example.sakhiExpensetTracker;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.sakhiExpensetTracker.domain.AppUser;
import com.example.sakhiExpensetTracker.domain.AppUserRepository;
import com.example.sakhiExpensetTracker.domain.Category;
import com.example.sakhiExpensetTracker.domain.CategoryRepository;




@SpringBootApplication
public class SakhiExpensetTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SakhiExpensetTrackerApplication.class, args);
	}
	

	/*
	@Bean
	public CommandLineRunner Bookstore(CategoryRepository crepository, AppUserRepository urepository) {
		return (args) -> {

			crepository.save(new Category("Hobbies"));
			crepository.save(new Category("Food"));
			crepository.save(new Category("Rent"));
			crepository.save(new Category("Transport"));
			crepository.save(new Category("Shopping"));
			crepository.save(new Category("Travelling"));
			crepository.save(new Category("Other"));
			
			// Create users: 
						AppUser user1 = new AppUser("user","user","user", "$2a$06$3jYRJrg0ghaaypjZ/.g4SethoeA51ph3UD4kZi9oPkeMTpjKU5uo6", "user@email.com", "USER", 0.0);
						AppUser user2 = new AppUser("admin", "admin","admin", "$2a$10$0MMwY.IQqpsVc1jC8u7IJ.2rT8b0Cd3b3sfIBGV2zfgnPGtT4r0.C", "admin@gmail.com", "ADMIN", 0.0);
						urepository.save(user1);
						urepository.save(user2);
			


		};
		
	}
	*/
	
	
		
	


}
