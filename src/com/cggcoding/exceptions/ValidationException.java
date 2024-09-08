package com.cggcoding.exceptions;

public class ValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ValidationException() {
		
	}

	public ValidationException(String message) {
		super(message);
		
	}

	public ValidationException(Throwable throwable) {
		super(throwable);
		
	}

	public ValidationException(String message, Throwable throwable) {
		super(message, throwable);
		
	}

	public ValidationException(String message, Throwable throwable, boolean arg2, boolean arg3) {
		super(message, throwable, arg2, arg3);
		
	}

}
