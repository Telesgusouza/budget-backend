package com.example.demo.exceptions;

public class AuthenticationRequiredOrFailed extends RuntimeException {
	private static final long serialVersionUID = 7366696342786923404L;
	
	public AuthenticationRequiredOrFailed(Object id) {
		super("Authentication required or failed. ID " + id);
	}

}
