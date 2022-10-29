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
public class EmployeeDto extends BaseAuditDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8874124628846019913L;
	private String id;
	private String username;
	private String fullname;
	private String email;
	private String image;
	private String idEmployee;
	private String lastEducation;
	private String idCardimage;
	private String parentId;
	private String parentName;
	private String occupationCode;
	private String occupationName;
	private String regionalId;
	private String regionalName;
	private String corporateCode;
	private String corporateName;

}