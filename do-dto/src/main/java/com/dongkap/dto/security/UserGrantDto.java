package com.dongkap.dto.security;

import java.util.ArrayList;
import java.util.List;

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
public class UserGrantDto extends ProfileDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1497632511578750619L;
	private boolean enabled = true;
	private boolean accountNonExpired = true;
	private boolean credentialsNonExpired = true;
	private boolean accountNonLocked = true;
	private String authorityDefault;
	private List<RoleDto> roles = new ArrayList<RoleDto>();

}