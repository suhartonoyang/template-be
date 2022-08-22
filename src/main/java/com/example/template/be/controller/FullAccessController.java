package com.example.template.be.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.template.be.bean.LogLevel;
import com.example.template.be.bean.RestResponse;
import com.example.template.be.bean.ValidationResult;
import com.example.template.be.model.BaseEntity;
import com.example.template.be.model.BaseEntityID;
import com.example.template.be.service.BaseService;
import com.example.template.be.util.LoggerHelper;

import io.swagger.annotations.ApiOperation;


/**
 * Here is our {@link id.co.fifgroup.template.core.controller.FullAccessController FullAccessController}
 * to give our controller all methods and endpoints for CRUD needs. 
 * <br /><br />
 * For example:
 * If we have RestController with prefix /api/v1/users, we got endpoint like this:<br />
 * - (Get All) GET /api/v1/users <br />
 * - (Insert) POST /api/v1/users <br />
 * - (Update) PUT /api/v1/users/{id} <br />
 * - (Delete) DELETE /api/v1/users/{id} <br /><br />
 * We can do filtering with Get All method by insert query param like this:<br />
 * - /api/v1/users?filter=name=Andy, age<30, height>180.0<br /><br />
 * We can do Pagination and Sorting with Get All method by insert query param like this:<br />
 * - /api/v1/users?page=1&pageSize=5&sort=+name, -age
 *
 * @param <T> Entity class
 * @param <ID> Data type class for ID
 * 
 * @author Andy (60833)
 *
 */
public abstract class FullAccessController<T extends BaseEntity, ID> extends ReadOnlyController<T, ID> {

	public FullAccessController(BaseService<T, ID> service) {
		super(service);
	}

	@ApiOperation(value = "Create One")
	@PostMapping
	public ResponseEntity<RestResponse<T>> create(@RequestBody T obj, HttpServletRequest req) {
		RestResponse<T> response = new RestResponse<T>(obj);
		
		ValidationResult validation = this.createValidation(obj);
		if(validation.getIsValid()) {
			this.log(req);
			
			try {
				obj = this.service.save(obj);
				response.setData(obj);
				
				return ResponseEntity.status(HttpStatus.CREATED).body(response);							
			}
			catch(Exception e) {
				LoggerHelper.log(this.logger, e.getMessage(), LogLevel.ERROR);
				
				response.setIsValid(false);
				response.setMessage("Something went wrong...");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		
		response.setIsValid(validation.getIsValid());
		response.setMessage(validation.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ApiOperation(value = "Update by ID")
	@PutMapping("/{id}")
	public ResponseEntity<RestResponse<T>> update(@PathVariable ID id, @RequestBody T obj, HttpServletRequest req) {
		RestResponse<T> response = new RestResponse<T>(obj);
		
		ValidationResult validation = this.updateValidation(obj, id);
		if(validation.getIsValid()) {
			this.log(req);
			
			try {
				obj = this.service.update(obj, id);
				response.setData(obj);
				
				return ResponseEntity.ok(response);				
			}
			catch(Exception e) {
				LoggerHelper.log(this.logger, e.getMessage(), LogLevel.ERROR);
				
				response.setIsValid(false);
				response.setMessage("Something went wrong...");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
		
		response.setIsValid(validation.getIsValid());
		response.setMessage(validation.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ApiOperation(value = "Delete by ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> delete(@PathVariable ID id, HttpServletRequest req) {
		this.log(req);
		
		try {
			return ResponseEntity.ok(this.service.delete(id));
		}
		catch(Exception e) {
			LoggerHelper.log(this.logger, e.getMessage(), LogLevel.ERROR);
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}
	}
	
	/**
	 * Override this method if you need additional create validation
	 * 
	 */
	protected ValidationResult customCreateValidation(T obj) {
		return new ValidationResult(true, null);
	}
	
	protected ValidationResult customUpdateValidation(T obj, ID id) {
		return new ValidationResult(true, null);
	}
	
	private ValidationResult createValidation(T obj) {
		ValidationResult customValidationResult = this.customCreateValidation(obj);
		if(obj instanceof BaseEntityID<?>) {
			String message = Optional.ofNullable(((BaseEntityID<?>) obj).getId() != null ? "ID must be null" : null).orElse(customValidationResult.getMessage());
			return new ValidationResult(((BaseEntityID) obj).getId() == null && customValidationResult.getIsValid(), message);			
		}
		
		return new ValidationResult(customValidationResult.getIsValid(), customValidationResult.getMessage());
	}
	
	private ValidationResult updateValidation(T obj, ID id) {
		ValidationResult customValidationResult = this.customUpdateValidation(obj, id);
		String message = Optional.ofNullable(id == null ? "ID is not exists" : null).orElse(customValidationResult.getMessage());
		return new ValidationResult(id != null && customValidationResult.getIsValid(), message);
	}
}
