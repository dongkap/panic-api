package com.dongkap.security.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongkap.common.exceptions.SystemErrorException;
import com.dongkap.common.pattern.PatternGlobal;
import com.dongkap.common.service.UserPrincipal;
import com.dongkap.common.utils.DateUtil;
import com.dongkap.common.utils.ErrorCode;
import com.dongkap.dto.common.ApiBaseResponse;
import com.dongkap.dto.security.ContactUserDto;
import com.dongkap.dto.security.EmergencyContactDto;
import com.dongkap.dto.security.PersonalInfoDto;
import com.dongkap.dto.security.ProfileDto;
import com.dongkap.dto.security.UserDto;
import com.dongkap.feign.service.ParameterI18nService;
import com.dongkap.feign.service.ProfileService;
import com.dongkap.security.dao.ContactUserRepo;
import com.dongkap.security.dao.EmergencyContactRepo;
import com.dongkap.security.dao.PersonalInfoRepo;
import com.dongkap.security.dao.UserRepo;
import com.dongkap.security.entity.ContactUserEntity;
import com.dongkap.security.entity.EmergencyContactEntity;
import com.dongkap.security.entity.PersonalInfoEntity;
import com.dongkap.security.entity.UserEntity;

@Service("profileService")
public class ProfileImplService implements ProfileService {

	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ContactUserRepo contactUserRepo;

	@Autowired
	private PersonalInfoRepo personalInfoRepo;

	@Autowired
	private EmergencyContactRepo emergencyContactRepo;
	
	@Autowired
	private ParameterI18nService parameterI18nService;

	@Override
	public UserDto getUser(String p_username, String p_locale) throws Exception {
		UserEntity user = this.userRepo.findByUsername(p_username);
		if(userRepo != null) {
			UserDto userDto = new UserDto();
			userDto.setUserId(user.getId());
			userDto.setUsername(user.getUsername());
			userDto.setEmail(user.getEmail());
			userDto.setName(user.getName());
			return userDto;
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
	}
	
	@Override
	public ProfileDto<?> getProfile(String p_username, String p_locale) throws Exception {
		if (p_username != null) {
			UserEntity user = userRepo.loadByUsername(p_username);
			ProfileDto<?> profileDto = new ProfileDto<>();
			profileDto.setUserId(user.getId());
			profileDto.setUsername(user.getUsername());
			profileDto.setEmail(user.getEmail());
			profileDto.setName(user.getName());
			profileDto.setContact(this.getContact(p_username, p_locale));
			profileDto.setPersonalInfo(this.getPersonalInfo(p_username, p_locale));
			return profileDto;
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
	}
	
	@Override
	public ProfileDto<?> getProfile(Authentication p_authentication, String p_locale) throws Exception {
		UserPrincipal user = (UserPrincipal) p_authentication.getPrincipal();
		if (user.getUsername() != null) {
			ProfileDto<?> profileDto = new ProfileDto<>();
			profileDto.setUserId(user.getId());
			profileDto.setUsername(user.getUsername());
			profileDto.setEmail(user.getEmail());
			profileDto.setName(user.getName());
			profileDto.setContact(this.getContact(p_authentication, p_locale));
			profileDto.setPersonalInfo(this.getPersonalInfo(p_authentication, p_locale));
			return profileDto;
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
	}

	@Override
	public ProfileDto<EmergencyContactDto> getProfileEmergency(String p_username, String p_locale) throws Exception {
		if (p_username != null) {
			UserEntity user = userRepo.loadByUsername(p_username);
			ProfileDto<EmergencyContactDto> profileDto = new ProfileDto<>();
			profileDto.setUserId(user.getId());
			profileDto.setUsername(user.getUsername());
			profileDto.setEmail(user.getEmail());
			profileDto.setName(user.getName());
			profileDto.setContact(this.getContact(p_username, p_locale));
			profileDto.setPersonalInfo(this.getPersonalInfo(p_username, p_locale));
			profileDto.setAdditionalInformation(this.getEmergencyContact(p_username, p_locale));
			return profileDto;
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
	}

	@Override
	public ContactUserDto getContact(Authentication p_authentication, String p_locale) throws Exception {
		UserPrincipal user = (UserPrincipal) p_authentication.getPrincipal();
		if (user.getUsername() != null) {
			return getContact(user.getUsername(), p_locale);
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
	}

	private ContactUserDto getContact(String p_username, String p_locale) throws Exception {
		ContactUserDto contactUserDto = new ContactUserDto();
		ContactUserEntity contactUser = this.contactUserRepo.findByUser_Username(p_username);
		if(contactUser != null) {
			contactUserDto.setAdministrativeAreaShort(contactUser.getAdministrativeAreaShort());
			contactUserDto.setAddress(contactUser.getAddress());
			contactUserDto.setCountry(contactUser.getCountry());
			contactUserDto.setProvince(contactUser.getProvince());
			contactUserDto.setCity(contactUser.getCity());
			contactUserDto.setDistrict(contactUser.getDistrict());
			contactUserDto.setSubDistrict(contactUser.getSubDistrict());
			contactUserDto.setZipcode(contactUser.getZipcode());
			contactUserDto.setPhoneNumber(contactUser.getPhoneNumber());
		}
		return contactUserDto;
	}

	@Override
	public PersonalInfoDto getPersonalInfo(Authentication p_authentication, String p_locale) throws Exception {
		UserPrincipal user = (UserPrincipal) p_authentication.getPrincipal();
		if (user.getUsername() != null) {
			return getPersonalInfo(user.getUsername(), p_locale);
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
	}

	private PersonalInfoDto getPersonalInfo(String p_username, String p_locale) throws Exception {
		PersonalInfoDto personalInfoDto = new PersonalInfoDto();
		PersonalInfoEntity personalInfo = this.personalInfoRepo.findByUser_Username(p_username);
		if(personalInfo != null) {
		    Calendar calDateOfBirth = Calendar.getInstance();
		    calDateOfBirth.setTime(personalInfo.getDateOfBirth());
		    Calendar calDateOfNow = Calendar.getInstance();
		    calDateOfNow.setTime(new Date());
		    personalInfoDto.setAge(calDateOfNow.get(Calendar.YEAR) - calDateOfBirth.get(Calendar.YEAR));
			personalInfoDto.setIdNumber(personalInfo.getIdNumber());
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("parameterCode", personalInfo.getGender());
			personalInfoDto.setGenderCode(personalInfo.getGender());
			try {
				personalInfoDto.setGender(parameterI18nService.getParameter(temp, p_locale).getParameterValue());
			} catch (Exception e) {}
			personalInfoDto.setPlaceOfBirth(personalInfo.getPlaceOfBirth());	
			personalInfoDto.setDateOfBirth(DateUtil.DATE.format(personalInfo.getDateOfBirth()));
			personalInfoDto.setImage(personalInfo.getImage());
		}
		return personalInfoDto;
	}

	@Override
	public EmergencyContactDto getEmergencyContact(Authentication p_authentication, String p_locale) throws Exception {
		UserPrincipal user = (UserPrincipal) p_authentication.getPrincipal();
		if (user.getUsername() != null) {
			return getEmergencyContact(user.getUsername(), p_locale);
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
	}

	private EmergencyContactDto getEmergencyContact(String p_username, String p_locale) throws Exception {
		EmergencyContactDto emergencyContactDto = new EmergencyContactDto();
		EmergencyContactEntity emergencyContact = this.emergencyContactRepo.findByUser_Username(p_username);
		if(emergencyContact != null) {
			emergencyContactDto.setReferenceName(emergencyContact.getReferenceName());
			emergencyContactDto.setReferenceAddress(emergencyContact.getReferenceAddress());
			emergencyContactDto.setReferencePhoneNumber(emergencyContact.getReferencePhoneNumber());
			emergencyContactDto.setRelationshipCode(emergencyContact.getRelationship());
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("parameterCode", emergencyContact.getRelationship());
			try {
				emergencyContactDto.setRelationship(parameterI18nService.getParameter(temp, p_locale).getParameterValue());
			} catch (Exception e) {}
		}
		return emergencyContactDto;
	}

	public ApiBaseResponse doUpdateProfile(ProfileDto<?> p_dto, Authentication p_authentication, String p_locale) throws Exception {
		this.doUpdateContact(p_dto.getContact(), p_authentication, p_locale);
		this.doUpdatePersonalInfo(p_dto.getPersonalInfo(), p_authentication, p_locale);
		return null;
	}

	@Transactional
	public ApiBaseResponse doUpdateContact(ContactUserDto p_dto, Authentication p_authentication, String p_locale) throws Exception {
		UserPrincipal user = (UserPrincipal) p_authentication.getPrincipal();
		if (user.getUsername() != null) {
			ContactUserEntity contactUser = this.contactUserRepo.findByUser_Username(user.getUsername());
			if (contactUser != null) {
				contactUser.setAdministrativeAreaShort(p_dto.getAdministrativeAreaShort());
				contactUser.setAddress(p_dto.getAddress());
				contactUser.setCountry(p_dto.getCountry());
				contactUser.setProvince(p_dto.getProvince());
				contactUser.setCity(p_dto.getCity());
				contactUser.setDistrict(p_dto.getDistrict());
				contactUser.setSubDistrict(p_dto.getSubDistrict());
				contactUser.setZipcode(p_dto.getZipcode());
				if (p_dto.getPhoneNumber() != null) {
					if (p_dto.getPhoneNumber().matches(PatternGlobal.PHONE_NUMBER.getRegex())) {
						contactUser.setPhoneNumber(p_dto.getPhoneNumber());	
					} else
						throw new SystemErrorException(ErrorCode.ERR_SCR0007A);
				}
				contactUser.setModifiedBy(user.getUsername());
				contactUser.setModifiedDate(new Date());
				this.contactUserRepo.save(contactUser);
			}
			return null;
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
	}

	@Transactional
	public ApiBaseResponse doUpdatePersonalInfo(PersonalInfoDto p_dto, Authentication p_authentication, String p_locale) throws Exception {
		UserPrincipal user = (UserPrincipal) p_authentication.getPrincipal();
		if (user.getUsername() != null) {
			PersonalInfoEntity personalInfo = this.personalInfoRepo.findByUser_Username(user.getUsername());
			if(personalInfo != null) {
				personalInfo.setIdNumber(p_dto.getIdNumber());
				personalInfo.setGender(p_dto.getGender());
				personalInfo.setPlaceOfBirth(p_dto.getPlaceOfBirth());
				personalInfo.setDateOfBirth(DateUtil.DATE.parse(p_dto.getDateOfBirth()));
				personalInfo.setModifiedBy(user.getUsername());
				personalInfo.setModifiedDate(new Date());
				this.personalInfoRepo.save(personalInfo);
			}
			return null;
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
	}

	@Transactional
	@Override
	public void doUpdatePhoto(Map<String, String> url, Authentication p_authentication, String locale) throws Exception {
		UserPrincipal user = (UserPrincipal) p_authentication.getPrincipal();
		if (user.getUsername() != null && url != null) {
			PersonalInfoEntity personalInfo = this.personalInfoRepo.findByUser_Username(user.getUsername());
			personalInfo.setImage(url.get("url"));
			personalInfo.setModifiedBy(user.getUsername());
			personalInfo.setModifiedDate(new Date());
			this.personalInfoRepo.save(personalInfo);
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
	}

}
