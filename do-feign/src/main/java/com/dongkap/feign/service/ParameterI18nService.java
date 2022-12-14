package com.dongkap.feign.service;

import java.util.List;
import java.util.Map;

import com.dongkap.dto.master.ParameterI18nDto;
import com.dongkap.dto.master.ParameterRequestDto;

public interface ParameterI18nService {
	
	public List<ParameterI18nDto> getParameterCode(Map<String, Object> filter) throws Exception;
	
	public List<ParameterI18nDto> getParameterCode(List<String> parameterCodes, String locale) throws Exception;
	
	public void postParameterI18n(ParameterRequestDto request, String username) throws Exception;
	
	public ParameterI18nDto getParameter(Map<String, Object> param, String locale) throws Exception;

}
