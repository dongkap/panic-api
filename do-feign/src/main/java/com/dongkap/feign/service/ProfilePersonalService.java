package com.dongkap.feign.service;

import org.springframework.security.core.Authentication;

import com.dongkap.dto.security.PersonalDto;

public interface ProfilePersonalService {
	
	public PersonalDto getProfilePersonal(Authentication authentication, String p_locale) throws Exception;

}
