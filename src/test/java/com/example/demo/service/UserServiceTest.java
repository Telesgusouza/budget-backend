package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.example.demo.config.TokenService;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.TokenException;
import com.example.demo.user.AuthenticationDTO;
import com.example.demo.user.RegisterDTO;
import com.example.demo.user.ResponseAccessAccountDTO;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import com.example.demo.user.UserRole;
import com.example.demo.user.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private TokenService tokenService;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService service;

	@Test
	void mustLogInSuccessfully() {

		AuthenticationDTO dto = new AuthenticationDTO("admin", "123");

		Authentication authentication = mock(Authentication.class);

		User user = mock(User.class);

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authentication);

		when(authentication.getPrincipal()).thenReturn(user);

		when(tokenService.generateToken(user)).thenReturn("token-teste");

		ResponseAccessAccountDTO response = service.login(dto);

		assertDoesNotThrow(() -> response);
		assertNotNull(response);
		assertEquals("token-teste", response.token());
	}

	@Test
	public void mustThrowTokenExceptionWhenGeneratingToken() {

		AuthenticationDTO dto = new AuthenticationDTO("admin", "123");

		Authentication authentication = mock(Authentication.class);

		User user = mock(User.class);

		when(authenticationManager.authenticate(any())).thenReturn(authentication);

		when(authentication.getPrincipal()).thenReturn(user);

		when(tokenService.generateToken(user)).thenThrow(new TokenException("Erro ao gerar token"));

		assertThrows(TokenException.class, () -> service.login(dto));
	}

	@Test
	public void mustThrowExceptionWhenCredentialsAreInvalid() {

		AuthenticationDTO dto = new AuthenticationDTO("adminError", "123");

		when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Credenciais inválidas"));

		assertThrows(BadCredentialsException.class, () -> service.login(dto));
	}

	@Test
	public void accountSuccessfullyCreated() {

		RegisterDTO dto = new RegisterDTO("adim", "123", UserRole.USER);

		User savedUser = new User(UUID.randomUUID(), dto.login(), "senha-criptografada", dto.role());

		when(userRepository.findByLogin(dto.login())).thenReturn(null);
		when(userRepository.save(any(User.class))).thenReturn(savedUser);
		when(tokenService.generateToken(savedUser)).thenReturn("generated-token-test");

		ResponseAccessAccountDTO response = service.register(dto);

		assertDoesNotThrow(() -> response);
		assertNotNull(response);
		assertEquals("generated-token-test", response.token());

	}

	@Test
	public void failedToRegister() {

		RegisterDTO dto = new RegisterDTO("adim", "123", UserRole.USER);

		User user = mock(User.class);

		when(this.userRepository.findByLogin(dto.login())).thenReturn(user);

		assertThrows(ResourceNotFoundException.class, () -> this.service.register(dto));

	}
}
