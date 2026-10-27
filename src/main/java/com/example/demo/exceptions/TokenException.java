package com.example.demo.exceptions;

public class TokenException extends RuntimeException {
	private static final long serialVersionUID = -6741444081457250941L;
	
	public TokenException(Object id) {
		super("Token exception. Id " + id);
	}

}
