package com.example.template.be.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.example.template.be.model.BaseEntity;
import com.example.template.be.model.BaseEntityID;
import com.example.template.be.repository.BaseRepository;
import com.example.template.be.util.LoggerHelper;

/**
 * All services should be extended from {@link this BaseService class}.
 * Why? Because all Controllers will refer to this class.
 *
 * @param <T> Entity class
 * @param <ID> Data type class for ID
 * 
 * @author Suhartono
 *
 */
public abstract class BaseService<T extends BaseEntity, ID> {
	
	protected Logger logger;
	
	@Autowired
	protected BaseRepository<T, ID> repo;
	
	public BaseService() {
		this.logger = LoggerFactory.getLogger(this.getClass());
	}
	
	/**
	 * 
	 * @param pageable must not be {@literal null}
	 * @return class Page with requested data
	 */
	public Page<T> findAll(Pageable pageable) {
		LoggerHelper.log(logger, "Find all data with pageable");
		return this.findAll(null, pageable);
	}
	
	/**
	 * 
	 * @param sort should not be {@literal null}. Use Sort.unsorted() instead
	 * @return List of requested data
	 */
	public List<T> findAll(Sort sort) {
		LoggerHelper.log(logger, "Find all data with sort");
		return this.findAll(null, sort);
	}
	
	/**
	 * 
	 * @param spec can be {@literal null}
	 * @param pageable must not be {@literal null}
	 * @return class Page with requested data
	 */
	public Page<T> findAll(Specification<T> spec, Pageable pageable) {
		LoggerHelper.log(logger, "Find all data with specification and pageable");
		return this.repo.findAll(spec, pageable);
	}
	
	/**
	 * 
	 * @param spec can be {@literal null}
	 * @param sort should not be {@literal null}. Use Sort.unsorted() instead
	 * @return List of requested data
	 */
	public List<T> findAll(Specification<T> spec, Sort sort) {
		LoggerHelper.log(logger, "Find all data with specification and sort");
		return this.repo.findAll(spec, sort);
	}
	
	
	public List<T> findAll(Specification<T> spec) {
		LoggerHelper.log(logger, "Find all data with specification");
		return this.repo.findAll(spec);
	}
	
	public T findById(ID id) {
		LoggerHelper.log(logger, "Find by ID : " + id);
		return this.repo.findById(id).orElse(null);
	}
	
	public T save(T obj) throws IllegalArgumentException {
		LoggerHelper.log(logger, "Save one data");
		return this.repo.save(obj);
	}
	
	public List<T> saveAll(List<T> objs) {
		LoggerHelper.log(logger, "Save many data");
		return this.repo.saveAll(objs);
	}

	@SuppressWarnings("unchecked")
	public T update(T obj, ID id) throws IllegalArgumentException {
		LoggerHelper.log(logger, "Update one data with ID : " + id);
		if(obj instanceof BaseEntityID<?>) {
			((BaseEntityID<ID>) obj).setId(id);
		}
		return this.save(obj);
	}

	public Boolean delete(ID id) throws IllegalArgumentException {
		LoggerHelper.log(logger, "Delete one data with ID : " + id);
		this.repo.deleteById(id);
		return true;
	}
	
}
