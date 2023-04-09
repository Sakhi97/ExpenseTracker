package com.example.sakhiExpensetTracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.sakhiExpensetTracker.domain.Category;
import com.example.sakhiExpensetTracker.domain.CategoryRepository;

@SpringBootTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@ActiveProfiles("test")

public class CategoryTests {

    // Inject the CategoryRepository bean
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testCreateCategory() {
        // Create a new category object
        Category category = new Category("Test");
        // Save the category object using the repository
        categoryRepository.save(category);
        // Assert that the category object has been saved successfully and has a non-null ID
        assertNotNull(category.getCategoryId());
     // Delete the category object using the repository
        categoryRepository.delete(category);
        
    }
    
    @Test
    public void testDeleteCategory() {
        // Create a new category object
        Category category = new Category("Test");
        // Save the category object using the repository
        categoryRepository.save(category);
        // Assert that the category object has been saved successfully and has a non-null ID
        assertNotNull(category.getCategoryId());
        
        // Delete the category object using the repository
        categoryRepository.delete(category);
        // Try to retrieve the deleted category object from the repository
        Optional<Category> deletedCategory = categoryRepository.findById(category.getCategoryId());
        // Assert that the deleted category object does not exist in the repository
        assertFalse(deletedCategory.isPresent());
    }


    @Test
    public void testGetCategoryByName() {
        // Create a new category object
        Category category = new Category("Travel");
        // Save the category object using the repository
        categoryRepository.save(category);
        // Retrieve the category from the repository by name
        List<Category> categories = categoryRepository.findByName("Travel");
        // Assert that only one category with the given name was found
        assertEquals(1, categories.size());
        // Assert that the retrieved category has the correct name
        assertEquals(category.getName(), categories.get(0).getName());
    }
}


