package com.dongkap.dto.activity;

import com.dongkap.dto.common.BaseApprovalDto;
import com.dongkap.dto.security.EmployeeDto;

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
public class AssignmentDto extends BaseApprovalDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8874124628846019913L;
	private String id;
	private EmployeeDto employee;

}