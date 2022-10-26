package com.dongkap.dto.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.dongkap.dto.checkbox.CheckboxDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestUserDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2624389791249022903L;
	private String username;
	private List<CheckboxDto> permissions = new ArrayList<CheckboxDto>();
	private String authorityDefault;
	private List<RoleDto> roles = new ArrayList<RoleDto>();

}
