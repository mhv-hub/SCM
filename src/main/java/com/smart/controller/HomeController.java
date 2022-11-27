package com.smart.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Below annotation is used to define the class as a controller which will be used to handel the controller method for urls.
// Whenever any url is hit from the View ( front end ), the controlle will execute the method which is mapped with that url.

@Controller
public class HomeController {

	private static final Logger logger = LogManager.getLogger(HomeController.class);

	// Below annotation is used to get object of this at the runtime. ( IOC
	// container will create the object and will inject it here )
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	// Below annotation is used to map the method with URL
	// The Model object used here is needed if any data needs to be sent to View.
	// Data can be sent to View using HttpSession object as well, but that can only
	// have key value pairs doe String.
	// To send any POJO object ro View, Model object is needed.
	// using model.addAttribute() method, we can send an object with a key name to
	// View.
	// on the View ( HTML code ) , we can use thymleaf to get the objects sent by
	// controller using model object.
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home - Smart Contact Manager");
		// Below return string is the name of html page that will be loaded when this
		// url is fired.
		return "home";
	}

	@RequestMapping("/about")
	public String about(Model model, HttpServletRequest req) {
		model.addAttribute("title", "About - Smart Contact Manager");
		return "about";
	}

	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Signup - Smart Contact Manager");
		model.addAttribute("user", new User());
		logger.error("***APP LOG || Signup page opened..................... ");
		return "signup";
	}

	@RequestMapping("/signin")
	public String login(Model model) {
		model.addAttribute("title", "Login - Smart Contact Manager");
		return "login";
	}

	// handler for registering user
	// Here @Valid is used for validation
	// @ModelAttribute("user") User user is telling that the user data sent by View
	// needs to be validated.
	// BidingResult result : this result object will store the result of validation
	// done by Spring.
	// Validation will be done based on the rules defined by validation annnotations
	// inside Entity class.
	// @RequestParam is used to get value from html form element. value =
	// 'aggrement' is the name of the html field whose value will be fetched.
	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,
			@RequestParam(value = "aggrement", defaultValue = "false") boolean aggrement, Model model,
			HttpSession session) {
		try {
			if (!aggrement) {
				throw new Exception("Please accept the terms and conditions !");
			}
			// this method will return true if any or all of the validation rules are
			// broken.
			if (result.hasErrors()) {
				System.out.println("ERRORR : " + result.toString());
				model.addAttribute("user", user);
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(false);
			user.setImageUrl("default.jpg");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			System.out.println(aggrement);
			System.out.println(user);

			// Adding object data to model. It will be sent back to View.
			model.addAttribute("user", new User());

			// This method will automatically save user data to table.
			// Possible because we have defined the class as entity and also used @Table to
			// define which table is related to this.
			this.userRepository.save(user);

			// Setting some string data to the key message, which will sent to View.
			session.setAttribute("message", new Message("Registered successfully !", "alert-success"));
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			if (e.getMessage() == "Please accept the terms and conditions !")
				session.setAttribute("message", new Message(e.getMessage(), "alert-warning"));
			else
				session.setAttribute("message",
						new Message("Something went wrong on the server !! " + e.getMessage(), "alert-danger"));
		}
		return "signup";
	}
}
