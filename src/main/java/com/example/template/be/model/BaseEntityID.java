package com.example.template.be.model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

/**
 * Every entities should be extended from
 * {@link BaseEntity BaseEntity} class. Why?
 * Because all Repositories, Services, Controllers will refer to this class
 *
 * @param <ID> Data type class for ID
 * 
 * @author Suhartono
 * 
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntityID<ID> extends BaseEntity {

	@Id
	private ID id;

	public Object getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
