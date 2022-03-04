package com.example.demo.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomException extends Exception {

	private static final long serialVersionUID = -5464016639706191661L;
	private int errorCode;
	private String message;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public CustomException(CustomExceptionMessages customExceptionMessages) {
		this.errorCode = customExceptionMessages.getId();
		this.message = customExceptionMessages.getMsg();
	}

}
