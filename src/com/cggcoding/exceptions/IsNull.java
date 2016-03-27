package com.cggcoding.exceptions;

public class IsNull extends ValidationException{

	public IsNull(){
	}
	
	public IsNull(String message) {
		super(message);
		
	}

	public IsNull(Throwable throwable) {
		super(throwable);
		
	}

	public IsNull(String message, Throwable throwable) {
		super(message, throwable);
		
	}

	public IsNull(String message, Throwable throwable, boolean arg2, boolean arg3) {
		super(message, throwable, arg2, arg3);
		
	}

}
