package com.example.template.be.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Suhartono
 *
 */
@Getter @Setter
public class ValidationResult {

	private Boolean isValid;
	private String message;
	
	public ValidationResult(Boolean isValid, String message) {
		this.isValid = isValid;
		this.message = message;
	}
	
}
