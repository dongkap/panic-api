package com.dongkap.security.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dongkap.security.entity.RoleEntity;

public interface RoleRepo extends JpaRepository<RoleEntity, String>, JpaSpecificationExecutor<RoleEntity> {

	RoleEntity findByAuthority(String authority);

	List<RoleEntity> findByAuthorityNotIn(List<String> authorities);

	List<RoleEntity> findByAuthorityIn(List<String> authorities);
	
}