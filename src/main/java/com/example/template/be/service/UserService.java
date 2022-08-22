package com.example.template.be.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.template.be.model.User;
import com.example.template.be.repository.RoleRepository;
import com.example.template.be.repository.UserRepository;

@Service
public class UserService extends BaseService<User, Long>{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
    private PasswordEncoder passwordEncoder;

	public User register(User newUser) {
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		newUser.setRoles(Collections.singletonList(roleRepository.findByNameContaining("USER")));
		User user = userRepository.findByUsername(newUser.getUsername());
		if (user==null) {
			return userRepository.save(newUser);
		}
		
		throw new RuntimeException("User already exists");
	}

}
