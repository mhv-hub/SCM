package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.dao.UserRepository;
import com.smart.entities.User;


public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	
	// We will overide this method and will provide a way to fetch the user details based on username.
	// Spring security will already have the username through login form.
	// Spring security will call this method to get the user details ( password, authorities)
	// This method call JPA get user details method and then stores all the details in CustomUserDetails object.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//Fetching user from database
		User user = userRepository.getUserByUserName(username);
		if(user == null) {
			throw new UsernameNotFoundException("Could not find user !!");
		}
		CustomUserDetails customUserDetails = new CustomUserDetails(user);
		return customUserDetails; 
	}

}
