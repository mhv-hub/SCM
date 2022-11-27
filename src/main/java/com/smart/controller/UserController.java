package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// If @RequestMapping is directly used on the class that means this url has some sub urls, so parent url is set on the entire class
// Sub urls will be mapped to the methods of this class
@Controller
@RequestMapping("/user")
public class UserController {

	private final Logger logger = LogManager.getLogger();

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	// This will be called every time handler is triggered.
	// Common method for all URL hits.
	@ModelAttribute
	private void addCommonData(Model m, Principal p) {

		// Here Principal object is used to return the username of current logged in
		// user.
		String username = p.getName();

		// Below method is used to get the details of user by using the usrname.
		User user = userRepository.getUserByUserName(username);
		m.addAttribute(user);
	}

	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {
		model.addAttribute("title", "User Dashboard");
		logger.info("User logged in : " + principal.getName());
		return "normal/user_dashboard";
	}

	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		model.addAttribute("title", "Add Contacts");
		return "normal/add_contact_form";
	}

	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute("contact") Contact contact,
			@RequestParam("profileImage") MultipartFile file, HttpSession session, Principal p, Model model) {
		try {
			User user = userRepository.getUserByUserName(p.getName());
			contact.setUser(user);
			user.getContacts().add(contact);

			if (!file.isEmpty()) {
				contact.setImage(file.getOriginalFilename());
				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image uploaded");
			} else {
				logger.error("No image provided by the user to create contact : " + p.getName());
				contact.setImage("contact.png");
			}

			userRepository.save(user);
			session.setAttribute("message", new Message("Contact saved successfully !! ", "alert-success"));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Save contact failed for the user : " + p.getName());
			session.setAttribute("message",
					new Message("Something went wrong on the server !! " + e.getMessage(), "alert-danger"));
			model.addAttribute("contact", contact);
		}
		return "normal/add_contact_form";
	}

	// per page 3(n)
	// current page 0(page)
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model model, Principal p) {
		model.addAttribute("title", "View Contacts");
		User user = this.userRepository.getUserByUserName(p.getName());

		PageRequest pageable = PageRequest.of(page, 3);

		Page<Contact> contacts = this.contactRepository.findContactByUser(user.getId(), pageable);
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());
		logger.info("User opened the contact list : " + p.getName());
		return "normal/show_contacts";
	}

	// show user details
	@RequestMapping("/contact/{cId}")
	public String showContactDetails(@PathVariable("cId") Integer cId, Model model) {
		System.out.println(cId);
		model.addAttribute("title", "Contact Details");
		Optional<Contact> contactOpt = this.contactRepository.findById(cId);
		Contact contact = contactOpt.get();
		model.addAttribute("contact", contact);
		return "normal/contact_details";
	}

	// delet user
	@GetMapping("/delete-contact/{cId}/{page}")
	public String deleteContact(@PathVariable("cId") Integer cId, @PathVariable("page") Integer page, Model model,
			Principal p) {

		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();

		User user = this.userRepository.getUserByUserName(p.getName());
		if (user.getId() == contact.getUser().getId()) {

			// Sometimes delete will not work due to mapping of contact with user, in that
			// case first we need to unlink
			// contact.setUser(null);

			// check how to delete image

			this.contactRepository.delete(contact);
			System.out.println("Contact deleted : " + contact.getEmail());
		}
		return "redirect:/user/show-contacts/" + page;
	}

	@GetMapping("/settings")
	public String openSettings(Model model) {
		model.addAttribute("title", "Settings");
		return "normal/settings";
	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPass") String oldPass, @RequestParam("newPass") String newPass,
			@RequestParam("newPassConf") String newPassConf, HttpSession session, Principal principal) {

		Message message = new Message();
		User user = this.userRepository.getUserByUserName(principal.getName());

		if (newPass.equals(newPassConf)) {
			if (this.bCryptPasswordEncoder.matches(oldPass, user.getPassword())) {
				if (newPass.equals(oldPass)) {
					message.setContent("Old password and new password can not be same !");
					message.setType("alert-warning");
				} else {
					user.setPassword(this.bCryptPasswordEncoder.encode(newPass));
					this.userRepository.save(user);
					message.setContent("Your password has been changed successfully !");
					message.setType("alert-success");
				}
			} else {
				message.setContent("Incorrect password ! Please enter your current password in the old password field");
				message.setType("alert-danger");
			}
		} else {
			message.setContent(
					"New passwords do not match ! Please provide same password in both new password and re-enter new password fields");
			message.setType("alert-warning");
		}
		session.setAttribute("message", message);
		return "normal/settings";
	}

}
