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
public class OccupationDto extends BaseAuditDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8874124628846019913L;
	private String id;
	private String code;
	private String name;
	private String corporateCode;
	private String corporateName;

}