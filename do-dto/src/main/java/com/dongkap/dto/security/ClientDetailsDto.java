package com.dongkap.dto.security;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class ClientDetailsDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8874124628846019913L;
	private String clientId;
	private String clientSecret;
	private String resourceIds;
	private String scope = "read,write,check_token";
	private String authorizedGrantTypes = "password,refresh_token";
	private String registeredRedirectUri = "";
	private String authorities = "SYS_END";
	private Integer accessTokenValidity = 3600;
	private Integer refreshTokenValidity = 2592000;
	private String additionalInformation;
	private Boolean autoApprove = true;

}