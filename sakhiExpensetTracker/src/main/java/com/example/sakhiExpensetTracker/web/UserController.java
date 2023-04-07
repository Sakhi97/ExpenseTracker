package com.example.sakhiExpensetTracker.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.sakhiExpensetTracker.domain.AppUser;
import com.example.sakhiExpensetTracker.domain.AppUserRepository;
import com.example.sakhiExpensetTracker.domain.SignupForm;

import jakarta.validation.Valid;



@Controller
public class UserController {
	@Autowired
    private AppUserRepository repository; 
	
    @RequestMapping(value = "signup")
    public String addStudent(Model model){
    	model.addAttribute("signupform", new SignupForm());
        return "signup";
    }	
    
    /**
     * Create new user
     * Check if user already exists & form validation
     * 
     * @param signupForm
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "saveuser", method = RequestMethod.POST)
    public String save(@Valid @ModelAttribute("signupform") SignupForm signupForm, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) { // validation errors
            if (signupForm.getPassword().equals(signupForm.getPasswordCheck())) { // check password match       
                String pwd = signupForm.getPassword();
                BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
                String hashPwd = bc.encode(pwd);

                AppUser newUser = new AppUser();
                newUser.setPasswordHash(hashPwd);
                newUser.setUsername(signupForm.getUsername());
                newUser.setFirstName(signupForm.getFirstName());
                newUser.setLastName(signupForm.getLastName());
                newUser.setRole("USER");
                newUser.setEmail(signupForm.getEmail());
                newUser.setBudget(0.0);
                
                if (repository.findByUsername(signupForm.getUsername()) != null) { // Check if user exists
                    bindingResult.rejectValue("username", "err.username", "Username already exists");     
                    return "signup";               
                }
                
                if (repository.findByEmail(signupForm.getEmail()) != null) { // Check if email exists
                    bindingResult.rejectValue("email", "err.email", "Email already exists");     
                    return "signup";               
                }
                
                // Save the user only if username and email are unique
                repository.save(newUser);
            }
            else {
                bindingResult.rejectValue("passwordCheck", "err.passCheck", "Passwords do not match");     
                return "signup";
            }
        }
        else {
            return "signup";
        }
        return "redirect:/login";     
    }

    
}