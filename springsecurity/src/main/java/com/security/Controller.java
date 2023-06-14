package com.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	
	@GetMapping("/hello")
	public String sayHello(String name) {
		return "Hello " + name;
				}
	
	@GetMapping("/helloadmin")
	public String sayAdminHello(String name) {
		return "Hello Admin  "+name;
	}
	}
