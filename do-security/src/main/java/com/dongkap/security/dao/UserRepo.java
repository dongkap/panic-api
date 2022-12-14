package com.dongkap.security.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dongkap.security.entity.UserEntity;

public interface UserRepo extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {

	@Query("SELECT u FROM UserEntity u JOIN FETCH u.roles r JOIN FETCH u.settings s WHERE LOWER(u.username) = :username OR LOWER(u.email) = :username")
	UserEntity loadByUsername(@Param("username") String username);

	@Query("SELECT u FROM UserEntity u WHERE LOWER(u.username) = :username OR LOWER(u.email) = :email")
	UserEntity loadByUsernameOrEmail(@Param("username") String username, @Param("email") String email);

	@Query("SELECT u FROM UserEntity u WHERE u.id = :id AND u.verificationCode = :verificationCode")
	UserEntity loadByIdAndVerificationCode(@Param("id") String id, @Param("verificationCode") String verificationCode);

	@Query("SELECT u FROM UserEntity u WHERE u.id = :id AND u.activateCode = :activateCode")
	UserEntity loadByIdAndActivateCode(@Param("id") String id, @Param("activateCode") String activateCode);

	@Query("SELECT u FROM UserEntity u WHERE u.id = :id AND u.email = :email")
	UserEntity loadByIdAndEmail(@Param("id") String id, @Param("email") String email);

	UserEntity findByUsername(String username);

	UserEntity findByUsernameAndEmail(String username, String email);

	UserEntity findByUsernameAndEmailAndProvider(String username, String email, String provider);

	Optional<UserEntity> findByEmail(String email);

	@Query("SELECT a.username FROM UserEntity a WHERE a.username = ?1 and a.active = true ")
	String validName(String var1);

	@Query("SELECT a FROM UserEntity a WHERE a.username = ?1 and a.active = 1")
	UserEntity valid(String var1);

}