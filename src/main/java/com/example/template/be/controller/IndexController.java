package com.example.template.be.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${rest.prefix}")
public class IndexController {

	@GetMapping
	public String index() {
		return "I am alive!";
	}
}
