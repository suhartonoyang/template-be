package com.example.template.be.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResponse<T> {

	private Boolean isValid;
	private String message;
	private T data;

	public RestResponse(T data, String message, Boolean isValid) {
		this.data = data;
		this.message = message;
		this.isValid = isValid;
	}

	public RestResponse(T data, String message) {
		this(data, message, message == null || message.equals(""));
	}

	public RestResponse(T data) {
		this(data, null);
	}

}
