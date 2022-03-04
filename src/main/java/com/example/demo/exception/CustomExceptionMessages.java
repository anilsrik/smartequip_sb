package com.example.demo.exception;

public enum CustomExceptionMessages {

	INVALID_INPUT_DATA(400, "Invalid Input Data"),
	INVALID_ANSWER(400, "Invalid Answer");

	private final int id;
	private final String msg;

	private CustomExceptionMessages(int id, String msg) {
		this.id = id;
		this.msg = msg;
	}

	public int getId() {
		return id;
	}

	public String getMsg() {
		return msg;
	}

}
