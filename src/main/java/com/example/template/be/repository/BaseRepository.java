package com.example.template.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.example.template.be.model.BaseEntity;

/**
 * All repositories should be extended from
 * {@link id.co.fifgroup.template.core.repository.BaseRepository
 * BaseRepository}. Why? Because all Services will refer to this class. <br />
 * We use {@link org.springframework.data.jpa.repository.JpaRepository
 * JpaRepository} as basic repository to find, save, etc and
 * {@link org.springframework.data.jpa.repository.JpaSpecificationExecutor
 * JpaSpecificationExecutor} for filtering purpose.
 * {@link org.springframework.data.jpa.repository.JpaRepository JpaRepository}
 * also provide Pagination and Sorting.
 *
 * @param <T>  Entity class
 * @param <ID> Data type class for ID
 * 
 * @author Suhartono
 *
 */
@NoRepositoryBean
public abstract interface BaseRepository<T extends BaseEntity, ID>
		extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

}
