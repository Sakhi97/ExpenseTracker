package com.example.sakhiExpensetTracker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.sakhiExpensetTracker.domain.AppUser;
import com.example.sakhiExpensetTracker.domain.AppUserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@ActiveProfiles("test")
public class AppUserTests {

    @Autowired
    private AppUserRepository userRepository;

   

    @Test
    public void createAppUser() {
        AppUser userTest = new AppUser("John", "Doe", "userTest", "testPassword", "usertest@email.com", "USER", 2000.0);
        userRepository.save(userTest);
        assertNotNull(userTest.getId());

        userRepository.delete(userTest); // Delete the user that was created during the test
        Optional<AppUser> deletedUser = userRepository.findById(userTest.getId());
        assertFalse(deletedUser.isPresent()); // Ensure that the deleted user does not exist in the repository
    }

    @Test
    public void deleteAppUser() {
        // Create a new AppUser object to be deleted
        AppUser userTestDelete = new AppUser("Jane", "Doe", "userTestDelete", "testPasswordd", "usertestd@email.com", "USER", 3000.0);
        // Save the new AppUser object using the repository
        userRepository.save(userTestDelete);
        // Retrieve the AppUser object to be deleted from the repository using the username
        AppUser userToDelete = userRepository.findByUsername("userTestDelete");
        // Delete the retrieved AppUser object from the repository
        userRepository.delete(userToDelete);
        // Try to retrieve the deleted AppUser object from the repository using the username
        AppUser userIsDeleted = userRepository.findByUsername("userTestDelete");
        // Assert that the deleted AppUser object does not exist in the repository
        assertNull(userIsDeleted);
    }

    
    @Test
    public void findByUsernameReturnUser() {
        // Create a user
    	AppUser testUser = new AppUser("Test", "User", "testUser", "password", "email", "USER", 1000.0);
        userRepository.save(testUser);

        // Test the findByUsername method
        AppUser findUser = userRepository.findByUsername(testUser.getUsername());
        assertThat(findUser.getUsername()).isEqualTo(testUser.getUsername());

        // Delete the user
        userRepository.delete(testUser);
    }


    @Test
    public void findByEmailReturnUser() {
    	
    	// Create a user
    	AppUser testUser = new AppUser("Test", "User", "testUser", "password", "email", "USER", 1000.0);
        userRepository.save(testUser);

     // Test the findByEmailReturnUser method
        AppUser findUser = userRepository.findByEmail(testUser.getEmail());
        assertThat(findUser.getEmail()).isEqualTo(testUser.getEmail());
        
        // Delete the user
        userRepository.delete(testUser);
    }
}

