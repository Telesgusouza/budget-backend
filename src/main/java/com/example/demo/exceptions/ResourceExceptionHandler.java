package com.example.demo.exceptions;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {

		String error = "Resource not found";
		Integer status = 404;
		StandardError err = new StandardError(Instant.now(), status, error, e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(status).body(err); 
	}

	@ExceptionHandler(TokenException.class)
	public ResponseEntity<StandardError> TokenException(TokenException e, HttpServletRequest request) {

		String error = "Token error";
		Integer status = 400;
		StandardError err = new StandardError(Instant.now(), status, error, e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(AuthenticationRequiredOrFailed.class)
	public ResponseEntity<StandardError> AuthenticationRequiredOrFailed(
			TokenException e, 
			HttpServletRequest request) {

		String error = "authentication required or failed";
		Integer status = 401;
		StandardError err = new StandardError(Instant.now(), status, error, e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}	
	
	
}
