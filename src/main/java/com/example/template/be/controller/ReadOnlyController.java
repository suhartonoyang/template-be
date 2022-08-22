package com.example.template.be.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.template.be.bean.PaginationAndSortResponse;
import com.example.template.be.bean.RestRequest;
import com.example.template.be.model.BaseEntity;
import com.example.template.be.service.BaseService;
import com.example.template.be.util.EntitySpecificationBuilder;
import com.example.template.be.util.LoggerHelper;

import io.swagger.annotations.ApiOperation;

/**
 * to give our controller all methods and endpoints only for get data purposes.
 * <br /><br />
 * For example:
 * If we have RestController with prefix /api/v1/users, we got endpoint like this:<br />
 * - (Get All) GET /api/v1/users <br /><br />
 * We can do filtering with Get All method by insert query param like this:<br />
 * - /api/v1/users?filter=name=Andy, age<30, height>180.0<br /><br />
 * We can do Pagination and Sorting with Get All method by insert query param like this:<br />
 * - /api/v1/users?page=1&pageSize=5&sort=+name, -age
 *
 * @param <T> Entity class
 * @param <ID> Data type class for ID
 * 
 * @author Suhartono
 *
 */
public abstract class ReadOnlyController<T extends BaseEntity, ID> {

	protected Logger logger;
	
	protected BaseService<T, ID> service;
	
	public ReadOnlyController(BaseService<T, ID> service) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.service = service;
	}
	
	@ApiOperation(value = "Get All")
	@GetMapping
	public ResponseEntity<PaginationAndSortResponse<T>> getPagingAndSortBy(@ModelAttribute RestRequest restReq, HttpServletRequest req) {
		
		this.log(req);
		
		Specification<T> specs = getSpecification(restReq.getFilter());
		
		if(restReq.getPage() == null && restReq.getPageSize() == null) {
			Sort sort = getSortFromRequest(restReq.getSort());
			
			PaginationAndSortResponse<T> response = new PaginationAndSortResponse<>();
			response.setData(this.beforeReturnResponse(this.service.findAll(specs, sort), restReq, req));
			
			return ResponseEntity.ok(response);
		}
		
		Pageable paginationAndSort = getPaginationAndSorting(restReq.getPage() - 1, restReq.getPageSize(), restReq.getSort());
		Page<T> result = this.service.findAll(specs, paginationAndSort);
		
		PaginationAndSortResponse<T> response = new PaginationAndSortResponse<>();
		response.setData(this.beforeReturnResponse(result.getContent(), restReq, req));
		response.setPage(result.getNumber() + 1);
		response.setPageSize(result.getNumberOfElements());
		response.setTotalAllData((int)result.getTotalElements());
		response.setTotalPages(result.getTotalPages());
		
		return ResponseEntity.ok(response);
	}
	
	@ApiOperation(value = "Get One")
	@GetMapping("/{id}")
	public ResponseEntity<T> getOne(@PathVariable ID id, @ModelAttribute RestRequest restReq, HttpServletRequest req) {
		this.log(req);
		
		T data = this.service.findById(id);
		return ResponseEntity.ok(this.beforeReturnResponse(data, restReq, req));
	}
	
	protected List<T> beforeReturnResponse(List<T> data, RestRequest restReq, HttpServletRequest req) {
		return data;
	}
	
	protected T beforeReturnResponse(T data, RestRequest restReq, HttpServletRequest req) {
		return data;
	}
	
	protected void log(HttpServletRequest req) {
		String message = String.format("[%s] %s", req.getMethod(), req.getRequestURI());
		LoggerHelper.log(logger, message);
	}
	
	private Pageable getPaginationAndSorting(Integer page, Integer pageSize, Set<String> sortList) {		
		if(page == null || pageSize == null) return null;
		
		Sort sort = getSortFromRequest(sortList);
		return PageRequest.of(page, pageSize, sort);
	}
	
	private Sort getSortFromRequest(Set<String> sortList) {
		if(sortList == null) return Sort.unsorted();
		
		Set<String> fieldSet = new HashSet<String>();
		Sort sort = null;
		
		for(String s : sortList) {
			Boolean hasOperator = s.startsWith("+") || s.startsWith("-");
			Boolean isDescending = s.charAt(0) == '-';
			
			String field = hasOperator ? s.substring(1) : s;
			if(fieldSet.contains(field)) continue;
				
			fieldSet.add(field);
			Sort itemSort = Sort.by(field);			
			if(isDescending) itemSort = itemSort.descending();
			
			if(sort == null) sort = itemSort;
			else sort = sort.and(itemSort);
		}
		
		if(sort == null) sort = Sort.unsorted();
		
		return sort;
	}
	
	private Specification<T> getSpecification(Set<String> filterSet) {
		if(filterSet == null) return null;
		
		EntitySpecificationBuilder<T, ID> builder = new EntitySpecificationBuilder<T, ID>();

		for(String filter : filterSet) {
			builder.add(filter);
		}
		
		return builder.build();
	}
}
