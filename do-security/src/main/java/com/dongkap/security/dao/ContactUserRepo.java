package com.dongkap.security.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dongkap.security.entity.ContactUserEntity;

public interface ContactUserRepo extends JpaRepository<ContactUserEntity, String>, JpaSpecificationExecutor<ContactUserEntity> {

	ContactUserEntity findByUser_Username(String username);

	List<ContactUserEntity> findByAdministrativeAreaShort(String administrativeAreaShort);

}