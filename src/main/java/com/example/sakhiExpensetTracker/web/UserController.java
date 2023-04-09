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
	// Autowire the user repository
	@Autowired
	private AppUserRepository userRepository;

	// Show the sign-up form
	@RequestMapping(value = "signup")
	public String addStudent(Model model) {
		model.addAttribute("signupform", new SignupForm());
		return "signup";
	}

	// Save a new user
	@RequestMapping(value = "saveuser", method = RequestMethod.POST)
	public String save(@Valid @ModelAttribute("signupform") SignupForm signupForm, BindingResult bindingResult) {
		// Check for validation errors
		if (!bindingResult.hasErrors()) {
			// Check if the passwords match
			if (signupForm.getPassword().equals(signupForm.getPasswordCheck())) {
				String pwd = signupForm.getPassword();
				BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
				String hashPwd = bc.encode(pwd);

				// Create a new user object and set its properties
				AppUser newUser = new AppUser();
				newUser.setPasswordHash(hashPwd);
				newUser.setUsername(signupForm.getUsername());
				newUser.setFirstName(signupForm.getFirstName());
				newUser.setLastName(signupForm.getLastName());
				newUser.setRole("USER");
				newUser.setEmail(signupForm.getEmail());
				newUser.setBudget(0.0);

				// Check if the username already exists
				if (userRepository.findByUsername(signupForm.getUsername()) != null) {
					bindingResult.rejectValue("username", "err.username", "Username already exists");
					return "signup";
				}

				// Check if the email already exists
				if (userRepository.findByEmail(signupForm.getEmail()) != null) {
					bindingResult.rejectValue("email", "err.email", "Email already exists");
					return "signup";
				}

				// Save the user only if username and email are unique
				userRepository.save(newUser);
			} else {
				// Passwords don't match
				bindingResult.rejectValue("passwordCheck", "err.passCheck", "Passwords do not match");
				return "signup";
			}
		} else {
			// Validation errors
			return "signup";
		}
		// Redirect to the login page after successful sign-up
		return "redirect:/login";
	}

	

}