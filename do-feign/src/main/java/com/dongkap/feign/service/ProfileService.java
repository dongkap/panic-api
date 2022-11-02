package com.dongkap.feign.service;

import java.util.Map;

import org.springframework.security.core.Authentication;

import com.dongkap.dto.security.ContactUserDto;
import com.dongkap.dto.security.EmergencyContactDto;
import com.dongkap.dto.security.PersonalInfoDto;
import com.dongkap.dto.security.ProfileDto;
import com.dongkap.dto.security.UserDto;

public interface ProfileService {
	
	public UserDto getUser(String p_username, String p_locale) throws Exception;
	
	public ProfileDto<?> getProfile(String p_username, String p_locale) throws Exception;
	
	public ProfileDto<?> getProfile(Authentication p_authentication, String p_locale) throws Exception;
	
	public ProfileDto<EmergencyContactDto> getProfileEmergency(String p_username, String p_locale) throws Exception;
	
	public ContactUserDto getContact(Authentication p_authentication, String p_locale) throws Exception;
	
	public PersonalInfoDto getPersonalInfo(Authentication p_authentication, String p_locale) throws Exception;
	
	public EmergencyContactDto getEmergencyContact(Authentication p_authentication, String p_locale) throws Exception;

	public void doUpdatePhoto(Map<String, String> url, Authentication p_authentication, String locale) throws Exception;

}
