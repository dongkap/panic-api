package com.dongkap.security.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dongkap.security.entity.ClientDetailsEntity;

public interface ClientDetailsRepo extends JpaRepository<ClientDetailsEntity, String>, JpaSpecificationExecutor<ClientDetailsEntity> {

	ClientDetailsEntity findByClientId(String clientId);

}