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
public class FunctionRoleRequestDto extends BaseAuditDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8874124628846019913L;
	private RoleDto role;
	private List<String> main = new ArrayList<String>();
	private List<String> extra = new ArrayList<String>();

}