package com.dongkap.dto.security;

import java.util.HashMap;
import java.util.Map;

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
public class EmployeeListDto extends BaseAuditDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8874124628846019913L;
	private String id;
	private String idEmployee;
	private String phoneNumber;
	private String address;
	private String occupationName;
	private Map<String, Object> user = new HashMap<String, Object>();

}