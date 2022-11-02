package com.dongkap.dto.security;

import com.dongkap.dto.common.BaseAuditDto;

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
public class ProfileDto<T extends Object> extends BaseAuditDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1742415621743889509L;
	private String userId;
	private String username;
	private String email;
	private String name;
	private ContactUserDto contact = new ContactUserDto();
	private PersonalInfoDto personalInfo = new PersonalInfoDto();
	private T additionalInformation;

}