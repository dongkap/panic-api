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
public class ActivateAccountDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8133839452021875038L;
	private String activateId;
	private String activateCode;
	private String password;
	private String confirmPassword;

}