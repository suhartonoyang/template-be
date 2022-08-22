package com.example.template.be.util;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.example.template.be.bean.LogLevel;
import com.example.template.be.bean.SearchCriteria;
import com.example.template.be.model.BaseEntity;

/**
 * to specific how our filtering will work. If you need
 * another operator, you should define how the operator works in toPredicate
 * method.
 *
 * @param <T>  Entity class
 * @param <ID> Data type class for ID
 * 
 * @author Suhartono
 *
 */
public class EntitySpecification<T extends BaseEntity, ID> implements Specification<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SearchCriteria criteria;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public EntitySpecification(SearchCriteria criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		String key = criteria.getKey().trim();
		Path<String> pathKey = root.get(key);
		String value = criteria.getValue().trim();

		LoggerHelper.log(logger,
				String.format("%s %s %s", key, criteria.getOperator(), criteria.getValue()),
				LogLevel.DEBUG);
		switch (criteria.getOperator()) {
			case "=":
				if(this.isArray(value)) {
					return this.getInClause(criteriaBuilder, pathKey, value);
				}
				
				return criteriaBuilder.equal(pathKey, value);
			case "=~":
				if(this.isArray(value)) {
					return this.getInClause(criteriaBuilder, pathKey, value, true);
				}
				
				return criteriaBuilder.equal(criteriaBuilder.lower(pathKey), value.toLowerCase());
			case "%":
				return criteriaBuilder.like(pathKey.as(String.class), String.format("%%%s%%", value));
			case "%~":
				return criteriaBuilder.like(criteriaBuilder.lower(pathKey.as(String.class)),
						String.format("%%%s%%", value.toLowerCase()));
			case ">>":
				return criteriaBuilder.like(pathKey.as(String.class), String.format("%s%%", value));
			case ">>~":
				return criteriaBuilder.like(criteriaBuilder.lower(pathKey.as(String.class)), String.format("%s%%", value.toLowerCase()));
			case "<<":
				return criteriaBuilder.like(pathKey.as(String.class), String.format("%%%s", value));
			case "<<~":
				return criteriaBuilder.like(criteriaBuilder.lower(pathKey.as(String.class)), String.format("%%%s", value.toLowerCase()));
			case "<":
				if (DateHelper.isDate(value)) {
					return criteriaBuilder.lessThan(pathKey.as(Date.class), DateHelper.convertToDate(value));
				}
	
				return criteriaBuilder.lessThan(pathKey, value);
			case "<=":
				if (DateHelper.isDate(value)) {
					return criteriaBuilder.lessThanOrEqualTo(pathKey.as(Date.class), DateHelper.convertToDate(value));
				}
	
				return criteriaBuilder.lessThanOrEqualTo(pathKey, value);
			case ">":
				if (DateHelper.isDate(value)) {
					return criteriaBuilder.greaterThan(pathKey.as(Date.class), DateHelper.convertToDate(value));
				}
	
				return criteriaBuilder.greaterThan(pathKey, value);
			case ">=":
				if (DateHelper.isDate(value)) {
					return criteriaBuilder.greaterThanOrEqualTo(pathKey.as(Date.class), DateHelper.convertToDate(value));
				}
	
				return criteriaBuilder.greaterThanOrEqualTo(pathKey, value);
			case "><":
				String[] values = value.split("&");
				String value1 = values[0].trim();
				String value2 = values[1].trim();
	
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (DateHelper.isDate(value1) && DateHelper.isDate(value2)) {
					predicates.add(criteriaBuilder.greaterThan(pathKey.as(Date.class), DateHelper.convertToDate(value1)));
					predicates.add(criteriaBuilder.lessThan(pathKey.as(Date.class), DateHelper.convertToDate(value2)));
				}
				else {
					predicates.add(criteriaBuilder.greaterThan(pathKey, value1));
					predicates.add(criteriaBuilder.lessThan(pathKey, value2));					
				}

				return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
			case ">=<":
				String[] bweValues = value.split("&");
				String bweValue1 = bweValues[0].trim();
				String bweValue2 = bweValues[1].trim();
	
				List<Predicate> bwePredicates = new ArrayList<Predicate>();
				if (DateHelper.isDate(bweValue1) && DateHelper.isDate(bweValue2)) {
					bwePredicates.add(criteriaBuilder.greaterThanOrEqualTo(pathKey.as(Date.class), DateHelper.convertToDate(bweValue1)));
					bwePredicates.add(criteriaBuilder.lessThanOrEqualTo(pathKey.as(Date.class), DateHelper.convertToDate(bweValue2)));
				}
				else {
					bwePredicates.add(criteriaBuilder.greaterThanOrEqualTo(pathKey, bweValue1));
					bwePredicates.add(criteriaBuilder.lessThanOrEqualTo(pathKey, bweValue2));					
				}

				return criteriaBuilder.and(bwePredicates.toArray(new Predicate[0]));
				
			default:
				return null;
		}
	}
	
	private In<String> getInClause(CriteriaBuilder criteriaBuilder, Path<String> pathKey, String value) {
		return this.getInClause(criteriaBuilder, pathKey, value, false);
	}
	
	private In<String> getInClause(CriteriaBuilder criteriaBuilder, Path<String> pathKey, String value, Boolean isIgnoreCase) {
		List<String> values = this.convertValueToList(value);
		In<String> inClause = criteriaBuilder.in(isIgnoreCase ? criteriaBuilder.lower(pathKey.as(String.class)) : pathKey.as(String.class));
		
		for(String v : values) {
			inClause.value(isIgnoreCase ? v.toLowerCase() : v);
		}

		return inClause;
	}
	
	private List<String> convertValueToList(String value) {
		return Arrays.asList(value.substring(1, value.length() - 1).split(";")).stream().map(String::trim).collect(Collectors.toList());
	}
	
	private Boolean isArray(String value) {
		return value.startsWith("[") && value.endsWith("]");
	}

}
