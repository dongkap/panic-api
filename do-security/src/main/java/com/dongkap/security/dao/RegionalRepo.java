package com.dongkap.security.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dongkap.security.entity.RegionalEntity;

public interface RegionalRepo extends JpaRepository<RegionalEntity, String>, JpaSpecificationExecutor<RegionalEntity> {

	List<RegionalEntity> findByIdIn(List<String> ids);

	List<RegionalEntity> findByAdministrativeAreaShort(String administrativeAreaShort);
	
}