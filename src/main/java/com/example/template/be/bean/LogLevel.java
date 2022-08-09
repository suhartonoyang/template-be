package com.example.template.be.bean;

import lombok.Getter;

@Getter
public enum LogLevel {

	OFF(0),
	FATAL(100),
	ERROR(200),
	WARN(300),
	INFO(400),
	DEBUG(500),
	TRACE(600);
	
	private final Integer code;
	
	private LogLevel(Integer code) {
		this.code = code;
	}
	
}
