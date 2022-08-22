package com.example.template.be.util;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;

import com.example.template.be.bean.SearchCriteria;
import com.example.template.be.model.BaseEntity;

/**
 * This is helper class to help us build a {@link Specification} class
 * which will be used in repository.
 * <br />
 * Use add() method to add filter needs.
 *
 * @param <T> Entity class
 * @param <ID> Data type class for ID
 * 
 * @author Suhartono
 *
 */
public class EntitySpecificationBuilder<T extends BaseEntity, ID> {

	private final List<SearchCriteria> criterias;
	
	private String prefix;
	
	private Boolean isRemovePrefix;
	
	public EntitySpecificationBuilder() {
		this.criterias = new Vector<SearchCriteria>();
		this.prefix = null;
		this.isRemovePrefix = false;
	}
	
	public EntitySpecificationBuilder(String prefix) {
		this();
		this.prefix = prefix.trim();
	}
	
	public EntitySpecificationBuilder(String prefix, Boolean isRemovePrefix) {
		this(prefix);
		this.isRemovePrefix = isRemovePrefix;
	} 
	
	public EntitySpecificationBuilder<T, ID> add(String key, String operator, String value) {
		if(this.prefix != null && !this.prefix.equals("") && key.startsWith(this.prefix)) {
			// For child purpose
			if(this.isRemovePrefix) {
				key = key.substring(this.prefix.length() + 1);
			}
			
			criterias.add(new SearchCriteria(key, operator, value));
			return this;
		}
		
		if(key.contains(".")) return this;
		
		criterias.add(new SearchCriteria(key, operator, value));
		return this;
	}
	
	public EntitySpecificationBuilder<T, ID> add(String criteria) {
		if(this.prefix != null && !this.prefix.equals("") && criteria.startsWith(this.prefix)) {
			if(this.isRemovePrefix) {
				criteria = criteria.substring(this.prefix.length() + 1);
			}
			
			criterias.add(new SearchCriteria(criteria));
			return this;
		}
		
		SearchCriteria sc = SearchCriteria.explodeCriteria(criteria);
		if(sc.getKey().contains(".")) return this;
		
		criterias.add(new SearchCriteria(criteria));
		return this;
	}
	
	public Specification<T> build() {
		if(criterias.isEmpty()) return null;
		
		List<Specification<T>> specs = criterias.stream()
			.map(c -> new EntitySpecification<T, ID>(c))
			.collect(Collectors.toList());
		
		Specification<T> result = specs.get(0);
		
		for(int i=1; i < specs.size(); ++i) {
			result = criterias.get(i).getIsOrPredicate() ?
					Specification.where(result).or(specs.get(i))
					: Specification.where(result).and(specs.get(i));
		}
		
		return result;
	}
	
}
