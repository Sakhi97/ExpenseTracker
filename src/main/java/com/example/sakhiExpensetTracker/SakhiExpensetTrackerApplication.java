package com.example.sakhiExpensetTracker;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.example.sakhiExpensetTracker.domain.AppUser;
import com.example.sakhiExpensetTracker.domain.AppUserRepository;
import com.example.sakhiExpensetTracker.domain.Category;
import com.example.sakhiExpensetTracker.domain.CategoryRepository;




@SpringBootApplication
public class SakhiExpensetTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SakhiExpensetTrackerApplication.class, args);
	}
	
	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
	

	
	@Bean
	public CommandLineRunner ExpenseTracker(CategoryRepository crepository, AppUserRepository urepository) {
	    return (args) -> {
	        String[] categoryNames = {
	            "Hobbies", "Food", "Rent", "Transport",
	            "Shopping", "Travelling", "Other"
	        };

	        for (String categoryName : categoryNames) {
	            try {
	                crepository.save(new Category(categoryName));
	            } catch (Exception e) {
	                System.err.println("Error occurred while saving category '" + categoryName + "': " + e.getMessage());
	            }
	        }

	        // Create users:
	        AppUser[] users = {
	            new AppUser("user", "user", "user", "$2a$06$3jYRJrg0ghaaypjZ/.g4SethoeA51ph3UD4kZi9oPkeMTpjKU5uo6", "user@email.com", "USER", 0.0),
	            new AppUser("admin", "admin", "admin", "$2a$10$0MMwY.IQqpsVc1jC8u7IJ.2rT8b0Cd3b3sfIBGV2zfgnPGtT4r0.C", "admin@gmail.com", "ADMIN", 0.0)
	        };

	        for (AppUser user : users) {
	            try {
	                urepository.save(user);
	            } catch (Exception e) {
	                System.err.println("Error occurred while saving user '" + user.getUsername() + "': " + e.getMessage());
	            }
	        }
	    };
	}

	
	
		
	


}
