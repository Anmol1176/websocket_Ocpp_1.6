package com.powerlogix.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.powerlogix.models.User;
import com.powerlogix.repo.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService 
{
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//fetching user from database
		
		 User   user = userRepository.getUserByUserName(username);

		 if(user==null)
		 {
			 throw new UsernameNotFoundException("Could Not Found User !");
		 }
		 
		 CustomUserDetails customUserDetails = new   CustomUserDetails(user);
		 
		return customUserDetails;
	}
}
