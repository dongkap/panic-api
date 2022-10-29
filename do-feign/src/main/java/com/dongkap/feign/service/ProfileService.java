package com.dongkap.feign.service;

import java.util.Map;

import org.springframework.security.core.Authentication;

import com.dongkap.dto.security.ContactUserDto;
import com.dongkap.dto.security.PersonalInfoDto;
import com.dongkap.dto.security.ProfileDto;

public interface ProfileService {
	
	public ProfileDto getProfile(Authentication p_authentication, String p_locale) throws Exception;
	
	public ContactUserDto getContact(Authentication p_authentication, String p_locale) throws Exception;
	
	public PersonalInfoDto getPersonalInfo(Authentication p_authentication, String p_locale) throws Exception;

	public void doUpdatePhoto(Map<String, String> url, Authentication p_authentication, String locale) throws Exception;

}
