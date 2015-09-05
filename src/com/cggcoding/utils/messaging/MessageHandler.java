package com.cggcoding.utils.messaging;

import java.util.List;

public class MessageHandler {
	private String errorMessage;
	private String successMessage;
	private String infoMessage;
	private List<String> errorMessageList;
	
	public MessageHandler(){
		this.errorMessage = "";
		this.successMessage = "";
		this.infoMessage = "";
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getInfoMessage() {
		return infoMessage;
	}

	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}
	
	public void clearMessages(){
		this.errorMessage = "";
		this.successMessage = "";
		this.infoMessage = "";
	}
}
