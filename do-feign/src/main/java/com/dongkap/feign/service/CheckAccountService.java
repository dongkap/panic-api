package com.dongkap.feign.service;

import java.util.Map;

import org.springframework.security.core.Authentication;

import com.dongkap.dto.common.ApiBaseResponse;

public interface CheckAccountService {
	
	public ApiBaseResponse doCheckPassword(Map<String, Object> dto, Authentication authentication, String p_locale) throws Exception;

}
