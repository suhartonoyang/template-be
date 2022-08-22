package com.example.template.be.bean;

import java.util.ArrayList;
import java.util.List;

import com.example.template.be.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * Custom response for each Pagination and Sorting endpoint
 * 
 * @author 60833
 *
 * @param <T>  Entity class
 * @param <ID> Data type class for ID
 */
@Getter
@Setter
public class PaginationAndSortResponse<T extends BaseEntity> {

	private Integer page;
	private Integer pageSize;
	private Integer totalPages;
	private Integer totalAllData;
	private List<T> data;

	public List<T> getData() {
		if (data == null)
			return new ArrayList<>();

		return data;
	}

}

