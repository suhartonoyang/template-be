package com.example.template.be.bean;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Andy (60833)
 *
 */
@Getter @Setter
public class SearchCriteria implements Serializable {
	
	/**
	 * Generated
	 */
	private static final long serialVersionUID = -6161557070884902521L;
	
	private String key;
	private String operator;
	private String value;
	private Boolean isOrPredicate;
	
	public SearchCriteria(String key, String operator, String value) {
		this.key = key;
		this.operator = operator;
		this.value = value;
		this.isOrPredicate = false;
	}
	
	/**
	 * 
	 * @param criteria has format as "<key><operator><value>" (e.g. name=john doe)
	 */
	public SearchCriteria(String criteria) {
		SearchCriteria sc = explodeCriteria(criteria);
		
		if(sc != null) {
			this.key = sc.getKey();
			this.operator = sc.getOperator();
			this.value = sc.getValue();
			this.isOrPredicate = sc.getIsOrPredicate();			
		}
	}
	
	public static SearchCriteria explodeCriteria(String criteria) {
		String[] operators = new String[] {
				"=", "=~", "<", "<=", ">", ">=", "%", "%~", "><", ">=<", ">>", ">>~", "<<", "<<~"
		};
		String patternStr = "(\\s*\\w[\\w\\s.]*)(" + String.join("|", operators) + ")(\\s*\\[?\\w[\\w\\s\\W]*)";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(criteria);
		if(matcher.find()) {
			/*
			 * if criteria is "name=john doe"
			 * the groups will be "name", "=", "john doe". Index starts with 1
			 * Tested in regex101.com
			 */
			SearchCriteria sc = new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3));
			return sc;
		}
		
		return null;
	}
	
}
