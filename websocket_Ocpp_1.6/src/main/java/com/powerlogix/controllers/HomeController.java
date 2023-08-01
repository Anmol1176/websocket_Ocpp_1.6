package com.powerlogix.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.powerlogix.models.User;
import com.powerlogix.repo.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController 
{
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/")
	public String indexPage(Model model) {
		model.addAttribute("title", "Login power logix application");
		return "index";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Register power logix application");
		model.addAttribute("user", new User());
		return "signup";
	}
	
	// handler for registeringuser
	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String registerUser(@ModelAttribute("user") User user, Model model,HttpSession session)
	{
		try {
						
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			System.out.println("USER" + user);

			User result = this.userRepository.save(user);

			model.addAttribute("user", new User());

			return "signup";
			
			

		} catch (Exception e) {
			e.printStackTrace();
			return "signup";
		}
		

	}
}

