package com.dongkap.security.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dongkap.common.utils.SchemaDatabase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "oauth_client_details", schema = SchemaDatabase.SECURITY)
public class ClientDetailsEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5870155744883664118L;

	@Id
	@Column(name = "client_id", nullable = false, unique = true)
	private String clientId;

	@Column(name = "client_secret", nullable = false)
	private String clientSecret;

	@Column(name = "resource_ids")
	private String resourceIds;

	@Column(name = "scope")
	private String scope = "read,write,check_token";

	@Column(name = "authorized_grant_types")
	private String authorizedGrantTypes = "password,refresh_token";

	@Column(name = "web_server_redirect_uri")
	private String registeredRedirectUri;

	@Column(name = "authorities")
	private String authorities = "SYS_END";

	@Column(name = "access_token_validity")
	private Integer accessTokenValidity = 3600;

	@Column(name = "refresh_token_validity")
	private Integer refreshTokenValidity = 2592000;

	@Column(name = "additional_information")
	private String additionalInformation;

	@Column(name = "autoapprove")
	private Boolean autoApprove = true;

}