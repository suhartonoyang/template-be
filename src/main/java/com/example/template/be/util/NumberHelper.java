package com.example.template.be.util;

public class NumberHelper {

	public static Boolean isNumber(String str) {
		return str.matches("\\d+");
	}
	
	public static Boolean isDecimal(String str) {
		return str.matches("\\d+.\\d+");
	}
	
}
