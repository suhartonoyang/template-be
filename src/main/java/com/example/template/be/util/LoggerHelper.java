package com.example.template.be.util;

import org.slf4j.Logger;

import com.example.template.be.bean.LogLevel;


public class LoggerHelper {

	public static void log(Logger logger, String message) {
		LoggerHelper.log(logger, message, LogLevel.INFO);
	}

	public static void log(Logger logger, String message, LogLevel logLevel) {
		switch (logLevel) {
		case ERROR:
			logger.error(message);
			break;
		case INFO:
			logger.info(message);
			break;
		case TRACE:
			logger.trace(message);
			break;
		case WARN:
			logger.warn(message);
			break;
		case DEBUG:
		default:
			logger.debug(message);
			break;
		}
	}

}