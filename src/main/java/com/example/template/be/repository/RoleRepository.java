package com.example.template.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.template.be.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	public Role findByNameContaining(String name);
}
