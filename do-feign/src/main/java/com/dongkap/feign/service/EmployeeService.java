package com.dongkap.feign.service;

import java.util.List;

import com.dongkap.dto.panic.FindNearestDto;

public interface EmployeeService {
	
	public List<String> getEmployeeNearest(FindNearestDto p_dto, String p_locale) throws Exception;
	
	public List<String> getEmployeeNearestIncludeAdmin(FindNearestDto p_dto, String p_locale) throws Exception;

}
