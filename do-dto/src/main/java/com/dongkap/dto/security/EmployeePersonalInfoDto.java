package com.dongkap.dto.security;

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
public class EmployeePersonalInfoDto extends EmployeeDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8874124628846019913L;
	private ContactUserDto contact;
	private PersonalInfoDto personalInfo;

}