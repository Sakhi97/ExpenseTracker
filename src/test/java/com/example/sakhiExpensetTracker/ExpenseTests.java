package com.example.sakhiExpensetTracker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.sakhiExpensetTracker.domain.AppUser;
import com.example.sakhiExpensetTracker.domain.AppUserRepository;
import com.example.sakhiExpensetTracker.domain.Category;
import com.example.sakhiExpensetTracker.domain.CategoryRepository;
import com.example.sakhiExpensetTracker.domain.Expense;
import com.example.sakhiExpensetTracker.domain.ExpenseRepository;


@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@ActiveProfiles("test")
public class ExpenseTests {

    @Autowired
    private ExpenseRepository expenceRepository;
    
    @Autowired
    private AppUserRepository appUserRepository;
    
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    
    @BeforeEach
    public void setUp() {
        expenceRepository.deleteAll();
        appUserRepository.deleteAll();
    }

    private Category findOrCreateCategory(String categoryName) {
        List<Category> categories = categoryRepository.findByName(categoryName);

        if (categories.isEmpty()) {
            return categoryRepository.save(new Category(categoryName));
        } else {
            return categories.get(0);
        }
    }


    @Test
    public void testCreateExpense() {
        Category category = findOrCreateCategory("Food");
        Expense expense = new Expense(100.0, LocalDate.now(), "Lunch", category);
        expenceRepository.save(expense);
        assertNotNull(expense.getId());
    }

    @Test
    public void testDeleteExpense() {
        Category category = findOrCreateCategory("Food");
        Expense expense = new Expense(100.0, LocalDate.now(), "Lunch", category);
        expenceRepository.save(expense);
        assertNotNull(expense.getId());
        expenceRepository.delete(expense);
        assertFalse(expenceRepository.findById(expense.getId()).isPresent());
    }

    @Test
    public void testFindByUser() {
        Category category1 = findOrCreateCategory("Food");
        Category category2 = findOrCreateCategory("Travel");
        // ...
    }

    @Test
    public void testFindByUserAndCategory() {
        Category category1 = findOrCreateCategory("Food");
        Category category2 = findOrCreateCategory("Travel");
        // ...
    }

    @Test
    public void testFindByUserAndDate() {
        Category category1 = findOrCreateCategory("Food");
        Category category2 = findOrCreateCategory("Transportation");
        // ...
    }

    @Test
    public void testFindByAppuserAndDateAndCategory() {
        Category category1 = findOrCreateCategory("Food");
        Category category2 = findOrCreateCategory("Transportation");
        // ...
    }

    // Modify other test cases similarly to use the findOrCreateCategory method
    
    @Test
    public void testFindByAppuserAndRemarkContainingIgnoreCase() {
        // create app user
        AppUser appUser = new AppUser("John", "Doe", "johndoe", "testPassword", "johndoe@example.com", "USER", 2000.0);
        appUserRepository.save(appUser);

        // create categories
        Category category1 = findOrCreateCategory("Food");

        // create expenses
        Expense expence1 = new Expense(100.0, LocalDate.now(), "Lunch", category1);
        expence1.setAppuser(appUser);
        expenceRepository.save(expence1);

        Expense expence2 = new Expense(50.0, LocalDate.now(), "Taxi ride", category1);
        expence2.setAppuser(appUser);
        expenceRepository.save(expence2);

        // search for expenses
        List<Expense> expenses = expenceRepository.findByAppuserAndRemarkContainingIgnoreCase(appUser, "taxi");
        assertThat(expenses.size()).isEqualTo(1);
        assertThat(expenses.get(0).getAmount()).isEqualTo(50.0);

        // delete expenses and categories
        expenceRepository.deleteAll();

        // delete app user
        appUserRepository.deleteAll();
    }

    @Test
    public void testFindByAppuserAndCategoryAndRemarkContainingIgnoreCase() {
        // create app user
        AppUser appUser = new AppUser("John", "Doe", "johndoe", "testPassword", "johndoe@example.com", "USER", 2000.0);
        appUserRepository.save(appUser);

        // create categories
        Category category1 = findOrCreateCategory("Food");
        Category category2 = findOrCreateCategory("Transportation");

        // create expenses
        Expense expence1 = new Expense(100.0, LocalDate.now(), "Lunch", category1);
        expence1.setAppuser(appUser);
        expenceRepository.save(expence1);

        Expense expence2 = new Expense(50.0, LocalDate.now(), "Taxi ride", category2);
        expence2.setAppuser(appUser);
        expenceRepository.save(expence2);

        // search for expenses
        List<Expense> expenses = expenceRepository.findByAppuserAndCategoryAndRemarkContainingIgnoreCase(appUser, category2, "taxi");
        assertThat(expenses.size()).isEqualTo(1);
        assertThat(expenses.get(0).getAmount()).isEqualTo(50.0);

        // delete expenses and categories
        expenceRepository.deleteAll();

        // delete app user
        appUserRepository.deleteAll();
    }
    
    @Test
    public void testFindByAppuserAndDateAndCategoryAndRemarkContainingIgnoreCase() {
        // create app user
        AppUser appUser = new AppUser("John", "Doe", "johndoe", "testPassword", "johndoe@example.com", "USER", 2000.0);
        appUserRepository.save(appUser);

        // create categories
        Category category1 = findOrCreateCategory("Food");

        // create expenses
        Expense expence1 = new Expense(100.0, LocalDate.now(), "Lunch", category1);
        expence1.setAppuser(appUser);
        expenceRepository.save(expence1);

        Expense expence2 = new Expense(50.0, LocalDate.now(), "Dinner", category1);
        expence2.setAppuser(appUser);
        expenceRepository.save(expence2);

        // search for expenses
        List<Expense> expenses = expenceRepository.findByAppuserAndDateAndCategoryAndRemarkContainingIgnoreCase(appUser, LocalDate.now(), category1, "lunch");
        assertThat(expenses.size()).isEqualTo(1);
        assertThat(expenses.get(0).getAmount()).isEqualTo(100.0);

        // delete expenses and categories
        expenceRepository.deleteAll();

        // delete app user
        appUserRepository.deleteAll();
    }




}

