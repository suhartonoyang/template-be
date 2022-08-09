package com.example.template.be.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.template.be.bean.LogLevel;

public class DateHelper {
	
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	
	private static Logger logger = LoggerFactory.getLogger(DateHelper.class);	

	public static Boolean isDate(String date) {
		return DateHelper.isDate(date, DateHelper.DEFAULT_DATE_FORMAT);
	}
	
	public static Boolean isDate(String date, String format) {
		LoggerHelper.log(logger, String.format("Trying parsing %s with format %s", date, format));
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
		
		try {
			sdf.parse(date);
			return true;
		}
		catch(ParseException e) {
			LoggerHelper.log(logger, e.getMessage(), LogLevel.WARN);
			return false;
		}
	}
	
	public static Date convertToDate(String date) {
		return DateHelper.convertToDate(date, DateHelper.DEFAULT_DATE_FORMAT);
	}
	
	public static Date convertToDate(String date, String format) {
		LoggerHelper.log(logger, String.format("Trying parsing %s with format %s", date, format));
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
		
		try {
			return sdf.parse(date);
		}
		catch(ParseException e) {
			LoggerHelper.log(logger, e.getMessage(), LogLevel.WARN);
			return null;
		}
	}
	
}
