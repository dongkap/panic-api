package com.dongkap.dto.security;

import java.util.ArrayList;
import java.util.List;

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
	private String idEmployee;
	private String lastEducation;
	private String username;
	private String password;
	private String fullname;
	private String email;
	private String image;
	private String parentId;
	private String parentLabel;
	private String parentValue;
	private OccupationDto occupation;
	private CorporateDto corporate;
	private List<RoleDto> roles = new ArrayList<RoleDto>();

}