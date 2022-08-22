package com.example.template.be.repository;

import org.springframework.stereotype.Repository;

import com.example.template.be.model.User;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {
	
	public User findByUsername(String username);
}
