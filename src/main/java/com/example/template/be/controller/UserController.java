package com.example.template.be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.template.be.model.User;
import com.example.template.be.service.UserService;

@RestController
@RequestMapping("${rest.prefix}/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public User register(@RequestBody User user) {
		return userService.saveOne(user);
	}
}
