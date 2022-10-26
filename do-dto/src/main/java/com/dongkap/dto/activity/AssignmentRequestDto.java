package com.dongkap.dto.activity;

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
public class AssignmentRequestDto extends AssignmentGroupDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8874124628846019913L;
	private List<String> employeeIds;
	private List<String> assignmentIds;

}