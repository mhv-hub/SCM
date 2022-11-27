package com.smart.controller;

import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotController {

	@RequestMapping("/forgot")
	public String openEmailForm(Model model) {
		model.addAttribute("title", "Forgot Password");
		return "forgot_form";
	}
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email-id") String email, Model model) {
		
		Random random = new Random(1000);
		random.nextInt(9999);
		model.addAttribute("title", "Enter OTP");
		return "verify_otp";
	}
	
	@PostMapping("/validate-otp")
	public String validateOTP(@RequestParam("otp") String otp, Model model) {
		
		
		model.addAttribute("title", "OTP Validation");
		return "change-password ";
	}
}
