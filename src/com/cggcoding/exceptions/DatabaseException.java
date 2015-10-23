package com.cggcoding.exceptions;

public class DatabaseException extends Exception{

	public DatabaseException() {
		
	}

	public DatabaseException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public DatabaseException(String message) {
		super(message);
	}

	public DatabaseException(Throwable message) {
		super(message);
	}

	
}
