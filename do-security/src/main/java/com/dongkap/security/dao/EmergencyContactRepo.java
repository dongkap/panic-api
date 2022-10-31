package com.dongkap.security.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dongkap.security.entity.EmergencyContactEntity;

public interface EmergencyContactRepo extends JpaRepository<EmergencyContactEntity, String>, JpaSpecificationExecutor<EmergencyContactEntity> {
	
	EmergencyContactEntity findByUser_Username(String username);
	
}