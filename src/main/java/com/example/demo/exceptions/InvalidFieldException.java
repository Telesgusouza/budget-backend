package com.example.demo.exceptions;

public class InvalidFieldException extends RuntimeException {
	private static final long serialVersionUID = -7442053601174041848L;

	public InvalidFieldException(Object id) {
		super("Invalid field exception. Id " + id);
	}
}
