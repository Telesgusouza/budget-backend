package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping
	public ResponseEntity<User> getUser(@AuthenticationPrincipal User user) {
		
		return ResponseEntity.ok().body(user);
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseAccessAccountDTO> login(@RequestBody @Valid AuthenticationDTO data) {
		
		
		ResponseAccessAccountDTO response = this.userService.login(data);
		
		return ResponseEntity.ok().body(response);
	}
	
	@PostMapping("/register")
	public ResponseEntity<ResponseAccessAccountDTO> register(@RequestBody @Valid RegisterDTO data) {
		
		ResponseAccessAccountDTO response = this.userService.register(data);
		
		return ResponseEntity.ok().body(response);
	}
	
}
