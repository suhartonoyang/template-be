package com.example.template.be.bean;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestRequest {

	/**
	 * Filter set consists of all filter needs. <br />
	 * Format: <strong>&lt;key&gt;&lt;operator&gt;&lt;value&gt;</strong> <br />
	 * <strong>&lt;operator&gt;</strong> should be either "=" or "&lt;" or "&gt;" or
	 * "&lt;=" or "&gt;=" or "%" (Operator like)
	 */
	private Set<String> filter;

	/**
	 * Sort set consists of all sort needs. <br />
	 * Format: <strong>&lt;operator&gt;&lt;field&gt;</strong> <br />
	 * <strong>&lt;operator&gt;</strong> should be either "+" for ascending or "-"
	 * for descending
	 */
	private Set<String> sort;

	private Integer page;
	private Integer pageSize;

}
