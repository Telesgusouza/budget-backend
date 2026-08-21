package com.example.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.config.TokenService;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenService tokenService;

	public ResponseAccessAccountDTO login(AuthenticationDTO data) {

		var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
		var auth = this.authenticationManager.authenticate(usernamePassword);

		var token = tokenService.generateToken((User) auth.getPrincipal());

		ResponseAccessAccountDTO response = new ResponseAccessAccountDTO(token);

		return response;
	}

	public ResponseAccessAccountDTO register(RegisterDTO data) {

		if (this.userRepository.findByLogin(data.login()) != null)
			throw new RuntimeException("Failed to register");

		String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
		User newUser = new User(null, data.login(), encryptedPassword, data.role());
		User user = this.userRepository.save(newUser);

		var token = this.tokenService.generateToken(user);

		ResponseAccessAccountDTO response = new ResponseAccessAccountDTO(token);

		return response;
	}

}
