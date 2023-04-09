package com.example.sakhiExpensetTracker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

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

    @Test
    public void testCreateExpense() {
        Category category = new Category("Food");
        Expense expense = new Expense(100.0, LocalDate.now(), "Lunch", category);
        expenceRepository.save(expense);
        assertNotNull(expense.getId());
    }
    
    @Test
    public void testDeleteExpense() {
        // create category and expense
        Category category = new Category("Food");
        Expense expense = new Expense(100.0, LocalDate.now(), "Lunch", category);
        expenceRepository.save(expense);

        // check that the expense was saved
        assertNotNull(expense.getId());

        // delete the expense
        expenceRepository.delete(expense);

        // check that the expense was deleted
        assertFalse(expenceRepository.findById(expense.getId()).isPresent());
    }
    
    @Test
    public void testFindByUser() {
        // Create two categories
        Category category1 = new Category("Food");
        Category category2 = new Category("Travel");
        // Create an app user and save to the database
        AppUser user = new AppUser("John", "Doe", "user1", "password", "user1@example.com", "USER", 2000.0);
        appUserRepository.save(user);
        // Save the categories to the database
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        // Create two expenses and set the app user and category for each expense
        Expense expense1 = new Expense(100.0, LocalDate.now(), "Lunch", category1);
        expense1.setAppuser(user);
        Expense expense2 = new Expense(200.0, LocalDate.now(), "Train ticket", category2);
        expense2.setAppuser(user);
        // Save the expenses to the database
        expenceRepository.save(expense1);
        expenceRepository.save(expense2);
        // Find all expenses for the given app user
        List<Expense> expenses = expenceRepository.findByAppuser(user);
        // Check that the list of expenses contains two elements with the correct amounts
        assertEquals(2, expenses.size());
        assertThat(expenses).extracting(Expense::getAmount).containsExactly(100.0, 200.0);
    }

    @Test
    public void testFindByUserAndCategory() {
        // Create two categories and save to the database
        Category category1 = new Category("Food");
        Category category2 = new Category("Travel");
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        // Create an app user and save to the database
        AppUser user = new AppUser("John", "Doe", "user1", "password", "user1@example.com", "USER", 2000.0);
        appUserRepository.save(user);
        // Create two expenses and set the app user and category for each expense
        Expense expense1 = new Expense(100.0, LocalDate.now(), "Lunch", category1);
        expense1.setAppuser(user);
        Expense expense2 = new Expense(200.0, LocalDate.now(), "Train ticket", category2);
        expense2.setAppuser(user);
        // Save the expenses to the database
        expenceRepository.save(expense1);
        expenceRepository.save(expense2);
        // Find all expenses for the given app user and category
        List<Expense> expenses = expenceRepository.findByAppuserAndCategory(user, category1);
        // Check that the list of expenses contains one element with the correct amount
        assertEquals(1, expenses.size());
        assertEquals(100.0, expenses.get(0).getAmount());
    }

    
    @Test
    public void testFindByUserAndDate() {
        // create app user
        AppUser appUser = new AppUser("John", "Doe", "johndoe", "testPassword", "johndoe@example.com", "USER", 2000.0);
        appUserRepository.save(appUser);

        // create categories
        Category category1 = new Category("Food");
        categoryRepository.save(category1);
        Category category2 = new Category("Transportation");
        categoryRepository.save(category2);

        // create expenses
        Expense expence1 = new Expense(100.0, LocalDate.now(), "Lunch", category1);
        expence1.setAppuser(appUser);
        expenceRepository.save(expence1);

        Expense expence2 = new Expense(50.0, LocalDate.now(), "Taxi ride", category2);
        expence2.setAppuser(appUser);
        expenceRepository.save(expence2);

        // search for expenses
        List<Expense> expenses = expenceRepository.findByAppuserAndDate(appUser, LocalDate.now());
        assertThat(expenses.size()).isEqualTo(2);

        // delete expenses and categories
        expenceRepository.deleteAll();
        categoryRepository.deleteAll();

        // delete app user
        appUserRepository.deleteAll();
    }
    
    @Test
    public void testFindByAppuserAndDateAndCategory() {
        // create app user
        AppUser appUser = new AppUser("John", "Doe", "johndoe", "testPassword", "johndoe@example.com", "USER", 2000.0);
        appUserRepository.save(appUser);

        // create categories
        Category category1 = new Category("Food");
        categoryRepository.save(category1);
        Category category2 = new Category("Transportation");
        categoryRepository.save(category2);

        // create expenses
        Expense expence1 = new Expense(100.0, LocalDate.now(), "Lunch", category1);
        expence1.setAppuser(appUser);
        expenceRepository.save(expence1);

        Expense expence2 = new Expense(50.0, LocalDate.now(), "Taxi ride", category2);
        expence2.setAppuser(appUser);
        expenceRepository.save(expence2);

        // search for expenses
        List<Expense> expenses = expenceRepository.findByAppuserAndDateAndCategory(appUser, LocalDate.now(), category1);
        assertThat(expenses.size()).isEqualTo(1);
        assertThat(expenses.get(0).getAmount()).isEqualTo(100.0);

        // delete expenses and categories
        expenceRepository.deleteAll();
        categoryRepository.deleteAll();

        // delete app user
        appUserRepository.deleteAll();
    }

    @Test
    public void testFindByAppuserAndRemarkContainingIgnoreCase() {
        // create app user
        AppUser appUser = new AppUser("John", "Doe", "johndoe", "testPassword", "johndoe@example.com", "USER", 2000.0);
        appUserRepository.save(appUser);

        // create categories
        Category category1 = new Category("Food");
        categoryRepository.save(category1);

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
        categoryRepository.deleteAll();

        // delete app user
        appUserRepository.deleteAll();
    }
    
    @Test
    public void testFindByAppuserAndCategoryAndRemarkContainingIgnoreCase() {
        // create app user
        AppUser appUser = new AppUser("John", "Doe", "johndoe", "testPassword", "johndoe@example.com", "USER", 2000.0);
        appUserRepository.save(appUser);

        // create categories
        Category category1 = new Category("Food");
        categoryRepository.save(category1);

        Category category2 = new Category("Transportation");
        categoryRepository.save(category2);

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
        categoryRepository.deleteAll();

        // delete app user
        appUserRepository.deleteAll();
    }

    @Test
    public void testFindByAppuserAndDateAndCategoryAndRemarkContainingIgnoreCase() {
        // create app user
        AppUser appUser = new AppUser("John", "Doe", "johndoe", "testPassword", "johndoe@example.com", "USER", 2000.0);
        appUserRepository.save(appUser);

        // create categories
        Category category1 = new Category("Food");
        categoryRepository.save(category1);

        // create expenses
        Expense expence1 = new Expense(100.0, LocalDate.now(), "Lunch", category1);
        expence1.setAppuser(appUser);
        expenceRepository.save(expence1);

        Expense expence2 = new Expense(50.0, LocalDate.now(), "Dinner", category1);
        expence2.setAppuser(appUser);
        expenceRepository.save(expence2);

        // search for expenses
        List<Expense> expenses = expenceRepository.findByAppuserAndDateAndCategoryAndRemarkContainingIgnoreCase(appUser, LocalDate.now(), category1, "Din");
        assertThat(expenses.size()).isEqualTo(1);
        assertThat(expenses.get(0).getAmount()).isEqualTo(50.0);

        // delete expenses and categories
        expenceRepository.deleteAll();
        categoryRepository.deleteAll();

        // delete app user
        appUserRepository.deleteAll();
    }


}
