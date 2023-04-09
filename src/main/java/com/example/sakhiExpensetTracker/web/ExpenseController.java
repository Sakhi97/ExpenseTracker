package com.example.sakhiExpensetTracker.web;



import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.sakhiExpensetTracker.domain.AppUser;
import com.example.sakhiExpensetTracker.domain.AppUserRepository;
import com.example.sakhiExpensetTracker.domain.Category;
import com.example.sakhiExpensetTracker.domain.CategoryRepository;
import com.example.sakhiExpensetTracker.domain.Expense;
import com.example.sakhiExpensetTracker.domain.ExpenseRepository;




@Controller
public class ExpenseController {

	// Autowire the expense repository
	@Autowired
	private ExpenseRepository repository;

	// Autowire the category repository
	@Autowired
	private CategoryRepository crepository;

	// Autowire the app user repository
	@Autowired
	private AppUserRepository userRepository;

	// The method to display the login page
	@RequestMapping(value="/login")
	public String login() {
	    return "login";
	}
	


	
    // The method to display the expense list
	@RequestMapping(value = { "/", "/expenselist" })
	public String expenseList(Model model) {
		// Get the currently logged in user
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentPrincipalName = authentication.getName();
	    AppUser user = userRepository.findByUsername(currentPrincipalName);
	    // Find all the expenses associated with the current user
	    List<Expense> expenses = repository.findByAppuser(user);
	        
	    // Calculate the total expenses
	    double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
	        
	    // Calculate the remaining budget
	    double remainingBudget = user.getBudget() - totalExpenses;
	    
	    // Calculate the budget
	    double budget = totalExpenses + remainingBudget;
	    
	    // Add the necessary attributes to the model
	    model.addAttribute("expenses", expenses);
	    model.addAttribute("totalExpenses", totalExpenses);
	    model.addAttribute("remainingBudget", remainingBudget);
	    model.addAttribute("budget", budget); 
	    model.addAttribute("categorys", crepository.findAll());
	    
	 // Return the expense list view
	    return "expenselist";
	}
	
	
	
    
 // RESTful service to get all expenses
    @RequestMapping(value="/expenses", method = RequestMethod.GET)
    public @ResponseBody List<Expense> expensesListRest() {
    	// Get the currently logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        AppUser user = userRepository.findByUsername(currentPrincipalName);
        
        return (List<Expense>) repository.findByAppuser(user);
    }

	// RESTful service to get expenses by id
    @RequestMapping(value="/expense/{id}", method = RequestMethod.GET)
    public @ResponseBody Optional<Expense> findExpenseRest(@PathVariable("id") Long expenseId) {	
    	return repository.findById(expenseId);
    }       

	// Add an expense
	@RequestMapping(value = "/add")
	public String addExpence(Model model) {

		model.addAttribute("expense", new Expense());
		model.addAttribute("categorys", crepository.findAll());
		return "addexpense";
	}
	// List users (only for admins)
	@RequestMapping(value = "/userlist", method = RequestMethod.GET)
	public String userList(Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentPrincipalName = authentication.getName();
	    AppUser currentUser = userRepository.findByUsername(currentPrincipalName);
	    
	    if (currentUser.isAdmin()) {
	        model.addAttribute("users", userRepository.findAll());
	        return "userlist";
	    }
	    
	    return "redirect:/expenselist"; // Redirect to another page if not an admin
	}
	

	
	
	//User changes email or password
	@RequestMapping(value = "/updateprofile", method = RequestMethod.POST)
	public String updateUserProfile(@RequestParam("username") String username, @RequestParam("firstname") String firstname, @RequestParam("lastname") String lastname, @RequestParam("email") String email, @RequestParam("password") String newPassword, @RequestParam("oldPassword") String oldPassword, Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentPrincipalName = authentication.getName();
	    AppUser currentUser = userRepository.findByUsername(currentPrincipalName);
	    
	    if (!username.isEmpty()) {
	        currentUser.setUsername(username);
	    }
	    if (!firstname.isEmpty()) {
	        currentUser.setFirstName(firstname);
	    }
	    if (!lastname.isEmpty()) {
	        currentUser.setLastName(lastname);
	    }
	    if (!email.isEmpty()) {
	        currentUser.setEmail(email);
	    }
	    
	    if (!newPassword.isEmpty() && !oldPassword.isEmpty()) {
	        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	        if (passwordEncoder.matches(oldPassword, currentUser.getPasswordHash())) {
	            currentUser.setPasswordHash(passwordEncoder.encode(newPassword));
	        } else {
	        	model.addAttribute("errorMessage", "Incorrect current password");
	            model.addAttribute("user", currentUser); // Add this line to include the user object in the model
	            return "userprofile"; // Assuming userprofile.html is the template for the profile page
	        }
	    }
	    
	    userRepository.save(currentUser);
	    return "redirect:/expenselist";
	}
	
	// Save an expense
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Expense expense) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        AppUser user = userRepository.findByUsername(currentPrincipalName);
        expense.setAppuser(user);
        repository.save(expense);
        return "redirect:expenselist";
    }
	// Delete an expense
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String deleteBook(@PathVariable("id") Long expenseId, Model model) {
		repository.deleteById(expenseId);
		return "redirect:../expenselist";
	}

	// Edit expense
	@RequestMapping(value = "/edit/{id}")
	public String editBook(@PathVariable("id") Long expenseId, Model model) {
		model.addAttribute("expense", repository.findById(expenseId));
		model.addAttribute("categorys", crepository.findAll());
		return "editexpense";
	}
	
	// update budget
		@RequestMapping(value = "/updatebudget", method = RequestMethod.POST)
		public String updateBudget(@RequestParam("budget") Double budget) {
			// Get the current authenticated user
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String currentPrincipalName = authentication.getName();
			AppUser currentUser = userRepository.findByUsername(currentPrincipalName);

			// Update the budget of the current user with the new value
			currentUser.setBudget(budget);
			userRepository.save(currentUser);

			return "redirect:/expenselist";
		}
		
		// Show the user's profile page
		@RequestMapping(value = "/userprofile", method = RequestMethod.GET)
		public String userProfile(Model model) {
			// Get the authenticated user's username
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String currentPrincipalName = authentication.getName();

			// Find the user object using the username
			AppUser currentUser = userRepository.findByUsername(currentPrincipalName);

			// Add the user object to the model
			model.addAttribute("user", currentUser);

			// Return the view name
			return "userprofile";
		}

		// delete a user
		@RequestMapping(value = "/deleteuser/{id}", method = RequestMethod.GET)
		@PreAuthorize("hasRole('ADMIN')")
		public String deleteUser(@PathVariable("id") Long userId, Model model) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String currentPrincipalName = authentication.getName();
			AppUser currentUser = userRepository.findByUsername(currentPrincipalName);

			AppUser userToDelete = userRepository.findById(userId).orElse(null);
			if (userToDelete != null) {
				// Delete all expenses associated with the user
				List<Expense> expensesToDelete = repository.findByAppuser(userToDelete);
				for (Expense expense : expensesToDelete) {
					repository.deleteById(expense.getId());
				}

				// Delete the user
				userRepository.deleteById(userId);
			}

			return "redirect:/userlist";
		}

		// Add to budget
		@RequestMapping(value = "/addtobudget", method = RequestMethod.POST)
		public String addToBudget(@RequestParam("budgetAdd") double budgetToAdd) {
			// Get the current authenticated user
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String currentPrincipalName = authentication.getName();
			AppUser currentUser = userRepository.findByUsername(currentPrincipalName);

			// Add the specified value to the current user's budget
			double newBudget = currentUser.getBudget() + budgetToAdd;
			currentUser.setBudget(newBudget);
			userRepository.save(currentUser);

			return "redirect:/expenselist";
		}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String searchExpences(@RequestParam(value = "searchDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate searchDate,
	                             @RequestParam(value = "searchCategory", required = false) Long searchCategoryId,
	                             @RequestParam(value = "searchRemark", required = false) String searchRemark,
	                             Model model) {
	    // Get the current user's authentication information
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentPrincipalName = authentication.getName();
	    // Find the current user in the app user repository
	    AppUser user = userRepository.findByUsername(currentPrincipalName);

	    List<Expense> expenses;
	    
	    // Get the search category from the category repository if the ID is not null, otherwise set it to null
	    Category searchCategory = searchCategoryId != null ? crepository.findById(searchCategoryId).orElse(null) : null;

	    // Perform different types of searches based on which search criteria are provided
	    if (searchDate != null && searchCategory != null && searchRemark != null) {
	        expenses = repository.findByAppuserAndDateAndCategoryAndRemarkContainingIgnoreCase(user, searchDate, searchCategory, searchRemark);
	    } else if (searchDate != null && searchCategory != null) {
	        expenses = repository.findByAppuserAndDateAndCategory(user, searchDate, searchCategory);
	    } else if (searchDate != null && searchRemark != null) {
	        expenses = repository.findByAppuserAndDateAndRemarkContainingIgnoreCase(user, searchDate, searchRemark);
	    } else if (searchCategory != null && searchRemark != null) {
	        expenses = repository.findByAppuserAndCategoryAndRemarkContainingIgnoreCase(user, searchCategory, searchRemark);
	    } else if (searchDate != null) {
	        expenses = repository.findByAppuserAndDate(user, searchDate);
	    } else if (searchCategory != null) {
	        expenses = repository.findByAppuserAndCategory(user, searchCategory);
	    } else if (searchRemark != null) {
	        expenses = repository.findByAppuserAndRemarkContainingIgnoreCase(user, searchRemark);
	    } else {
	        expenses = repository.findByAppuser(user);
	    }

	    // Calculate total expenses and remaining budget
	    double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
	    double remainingBudget = user.getBudget() - totalExpenses;

	    // Add the search results and other data to the model
	    model.addAttribute("expenses", expenses);
	    model.addAttribute("totalExpenses", totalExpenses);
	    model.addAttribute("remainingBudget", remainingBudget);
	    model.addAttribute("categorys", crepository.findAll());

	    // Return the view that displays the search results
	    return "expenselist";
	}

}
