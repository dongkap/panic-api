package com.dongkap.notification.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dongkap.notification.entity.WebSubscriptionEntity;

public interface WebSubscriptionRepo extends JpaRepository<WebSubscriptionEntity, String>, JpaSpecificationExecutor<WebSubscriptionEntity> {
	
	WebSubscriptionEntity findByEndpoint(String endpoint);
	
	WebSubscriptionEntity findByUsername(String username);

}