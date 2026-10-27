package com.example.demo.exceptions;

public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 8289029752293762994L;

	public ResourceNotFoundException(Object id) {
		super("Resource not found. Id " + id);
	}
	
}
