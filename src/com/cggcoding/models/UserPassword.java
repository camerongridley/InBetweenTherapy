package com.cggcoding.models;

public class UserPassword {
	private byte[] encryptedPassword;
	private byte[] passwordSalt;
	
	public UserPassword() {
		
	}

	public UserPassword(byte[] encryptedPassword, byte[] passwordSalt) {
		super();
		this.encryptedPassword = encryptedPassword;
		this.passwordSalt = passwordSalt;
	}

	public byte[] getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(byte[] encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public byte[] getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(byte[] passwordSalt) {
		this.passwordSalt = passwordSalt;
	}
	
	

}
