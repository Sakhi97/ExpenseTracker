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
import com.example.sakhiExpensetTracker.domain.Expence;
import com.example.sakhiExpensetTracker.domain.ExpenceRepository;




@Controller
public class ExpenceController {

	@Autowired
	private ExpenceRepository repository;

	@Autowired
	private CategoryRepository crepository;
	
	@Autowired
    private AppUserRepository userRepository;
	
	//Login page
	@RequestMapping(value="/login")
	public String login() {
		return "login";
	}
	


	
    // List expenses
	@RequestMapping(value = { "/", "/expencelist" })
	public String expenseList(Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentPrincipalName = authentication.getName();
	    AppUser user = userRepository.findByUsername(currentPrincipalName);
	    List<Expence> expenses = repository.findByAppuser(user);
	        
	    // Calculate the total expenses
	    double totalExpenses = expenses.stream().mapToDouble(Expence::getAmount).sum();
	        
	    // Calculate the remaining budget
	    double remainingBudget = user.getBudget() - totalExpenses;
	    
	    // Calculate the budget
	    double budget = totalExpenses + remainingBudget;
	        
	    model.addAttribute("expences", expenses);
	    model.addAttribute("totalExpenses", totalExpenses);
	    model.addAttribute("remainingBudget", remainingBudget);
	    model.addAttribute("budget", budget); 
	    model.addAttribute("categorys", crepository.findAll());
	    return "expencelist";
	}
	
	
	
    
 // RESTful service to get all expenses
    @RequestMapping(value="/expenses", method = RequestMethod.GET)
    public @ResponseBody List<Expence> expensesListRest() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        AppUser user = userRepository.findByUsername(currentPrincipalName);
        return (List<Expence>) repository.findByAppuser(user);
    }

	// RESTful service to get expenses by id
    @RequestMapping(value="/expence/{id}", method = RequestMethod.GET)
    public @ResponseBody Optional<Expence> findExpenceRest(@PathVariable("id") Long expenceId) {	
    	return repository.findById(expenceId);
    }       

	// Add an expense
	@RequestMapping(value = "/add")
	public String addExpence(Model model) {

		model.addAttribute("expence", new Expence());
		model.addAttribute("categorys", crepository.findAll());
		return "addexpence";
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
	    
	    return "redirect:/expencelist"; // Redirect to another page if not an admin
	}
	

	@RequestMapping(value = "/userprofile", method = RequestMethod.GET)
	public String userProfile(Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentPrincipalName = authentication.getName();
	    AppUser currentUser = userRepository.findByUsername(currentPrincipalName);
	    model.addAttribute("user", currentUser);
	    return "userprofile";
	}
	
	//change email or password
	@RequestMapping(value = "/updateprofile", method = RequestMethod.POST)
	public String updateUserProfile(@RequestParam("email") String email, @RequestParam("password") String password) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentPrincipalName = authentication.getName();
	    AppUser currentUser = userRepository.findByUsername(currentPrincipalName);
	    
	    currentUser.setEmail(email);
	    currentUser.setPasswordHash(new BCryptPasswordEncoder().encode(password));
	    userRepository.save(currentUser);
	    
	    return "redirect:/expencelist";
	}
	
	//update budget
	@RequestMapping(value = "/updatebudget", method = RequestMethod.POST)
	public String updateBudget(@RequestParam("budget") Double budget) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentPrincipalName = authentication.getName();
	    AppUser currentUser = userRepository.findByUsername(currentPrincipalName);
	    
	    currentUser.setBudget(budget);
	    userRepository.save(currentUser);
	    
	    return "redirect:/expencelist";
	}


	
	
	//delete a user
	@RequestMapping(value = "/deleteuser/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN')")
	public String deleteUser(@PathVariable("id") Long userId, Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentPrincipalName = authentication.getName();
	    AppUser currentUser = userRepository.findByUsername(currentPrincipalName);

	    AppUser userToDelete = userRepository.findById(userId).orElse(null);
	    if (userToDelete != null) {
	        // Delete all expenses associated with the user
	        List<Expence> expensesToDelete = repository.findByAppuser(userToDelete);
	        for (Expence expense : expensesToDelete) {
	            repository.deleteById(expense.getId());
	        }

	        // Delete the user
	        userRepository.deleteById(userId);
	    }

	    return "redirect:/userlist";
	}


	
	//Add to budget
	@RequestMapping(value = "/addtobudget", method = RequestMethod.POST)
	public String addToBudget(@RequestParam("budgetAdd") double budgetToAdd) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentPrincipalName = authentication.getName();
	    AppUser currentUser = userRepository.findByUsername(currentPrincipalName);
	    
	    double newBudget = currentUser.getBudget() + budgetToAdd;
	    currentUser.setBudget(newBudget);
	    userRepository.save(currentUser);
	    
	    return "redirect:/expencelist";
	}
	// Save an expense
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Expence expence) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        AppUser user = userRepository.findByUsername(currentPrincipalName);
        expence.setAppuser(user);
        repository.save(expence);
        return "redirect:expencelist";
    }
	// Delete an expense
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String deleteBook(@PathVariable("id") Long expenceId, Model model) {
		repository.deleteById(expenceId);
		return "redirect:../expencelist";
	}

	// Edit expense
	@RequestMapping(value = "/edit/{id}")
	public String editBook(@PathVariable("id") Long expenceId, Model model) {
		model.addAttribute("expence", repository.findById(expenceId));
		model.addAttribute("categorys", crepository.findAll());
		return "editexpence";
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String searchExpences(@RequestParam(value = "searchDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate searchDate,
	                             @RequestParam(value = "searchCategory", required = false) Long searchCategoryId,
	                             @RequestParam(value = "searchRemark", required = false) String searchRemark,
	                             Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentPrincipalName = authentication.getName();
	    AppUser user = userRepository.findByUsername(currentPrincipalName);

	    List<Expence> expenses;
	    
	    Category searchCategory = searchCategoryId != null ? crepository.findById(searchCategoryId).orElse(null) : null;

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

	    double totalExpenses = expenses.stream().mapToDouble(Expence::getAmount).sum();
	    double remainingBudget = user.getBudget() - totalExpenses;

	    model.addAttribute("expences", expenses);
	    model.addAttribute("totalExpenses", totalExpenses);
	    model.addAttribute("remainingBudget", remainingBudget);
	    model.addAttribute("categorys", crepository.findAll());

	    return "expencelist";
	}

}
