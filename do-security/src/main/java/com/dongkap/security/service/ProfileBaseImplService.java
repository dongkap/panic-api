package com.dongkap.security.service;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongkap.common.exceptions.SystemErrorException;
import com.dongkap.common.pattern.PatternGlobal;
import com.dongkap.common.utils.ErrorCode;
import com.dongkap.dto.common.ApiBaseResponse;
import com.dongkap.dto.security.ProfileDto;
import com.dongkap.feign.service.ProfileService;
import com.dongkap.security.dao.ContactUserRepo;
import com.dongkap.security.dao.UserRepo;
import com.dongkap.security.entity.ContactUserEntity;
import com.dongkap.security.entity.UserEntity;
import com.dongkap.common.service.UserPrincipal;

@Service("profileBaseService")
public class ProfileBaseImplService implements ProfileService {

	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ContactUserRepo contactUserRepo;

	@Autowired
	private UserRepo userRepo;

	@Transactional
	public ApiBaseResponse doUpdateProfileBase(ProfileDto p_dto, UserPrincipal p_user, String p_locale) throws Exception {
		if (p_user.getUsername() != null) {
			ContactUserEntity profile = this.contactUserRepo.findByUser_Username(p_user.getUsername());
			if (profile != null) {
				profile.setAddress(p_dto.getAddress());
				profile.setCountry(p_dto.getCountry());
				profile.setProvince(p_dto.getProvince());
				profile.setCity(p_dto.getCity());
				profile.setDistrict(p_dto.getDistrict());
				profile.setSubDistrict(p_dto.getSubDistrict());
				profile.setZipcode(p_dto.getZipcode());
				profile.setDescription(p_dto.getDescription());
				if (p_dto.getEmail() != null) {
					if (p_dto.getEmail().matches(PatternGlobal.EMAIL.getRegex())) {
						p_user.setEmail(p_dto.getEmail());	
					} else
						throw new SystemErrorException(ErrorCode.ERR_SCR0008);
				}
				if (p_dto.getPhoneNumber() != null) {
					if (p_dto.getPhoneNumber().matches(PatternGlobal.PHONE_NUMBER.getRegex())) {
						profile.setPhoneNumber(p_dto.getPhoneNumber());	
					} else
						throw new SystemErrorException(ErrorCode.ERR_SCR0007A);
				}
				profile.setModifiedBy(p_user.getUsername());
				profile.setModifiedDate(new Date());
				this.contactUserRepo.save(profile);
			}
			return null;
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
	}
	
	public ProfileDto getProfileBase(UserPrincipal p_user, String p_locale) throws Exception {
		if (p_user.getUsername() != null) {
			ProfileDto dto = new ProfileDto();
			ContactUserEntity profile = this.contactUserRepo.findByUser_Username(p_user.getUsername());
			dto.setUsername(p_user.getUsername());
			dto.setEmail(p_user.getEmail());
			dto.setAddress(profile.getAddress());
			dto.setCountry(profile.getCountry());
			dto.setProvince(profile.getProvince());
			dto.setCity(profile.getCity());
			dto.setDistrict(profile.getDistrict());
			dto.setSubDistrict(profile.getSubDistrict());
			dto.setZipcode(profile.getZipcode());
			dto.setImage(profile.getUser().getImage());
			dto.setPhoneNumber(profile.getPhoneNumber());
			dto.setDescription(profile.getDescription());
			return dto;
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
	}
	
	public ProfileDto getBaseProfileAuth(Map<String, Object> param, String p_locale) throws Exception {
		if (!param.isEmpty()) {
			ProfileDto dto = new ProfileDto();
			ContactUserEntity profile = this.contactUserRepo.findByUser_Username(param.get("username").toString());
			dto.setUsername(param.get("username").toString());
			dto.setEmail(profile.getUser().getEmail());
			dto.setAddress(profile.getAddress());
			dto.setCountry(profile.getCountry());
			dto.setProvince(profile.getProvince());
			dto.setCity(profile.getCity());
			dto.setDistrict(profile.getDistrict());
			dto.setSubDistrict(profile.getSubDistrict());
			dto.setZipcode(profile.getZipcode());
			dto.setImage(profile.getUser().getImage());
			dto.setPhoneNumber(profile.getPhoneNumber());
			dto.setDescription(profile.getDescription());
			return dto;
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
	}

	@Transactional
	@Override
	public void doUpdatePhoto(Map<String, String> url, Authentication authentication, String locale) throws Exception {
		UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
		if (user.getUsername() != null && url != null) {
			UserEntity userEntity = this.userRepo.findByUsername(user.getUsername());
			userEntity.setImage(url.get("url"));
			userEntity.setModifiedBy(user.getUsername());
			userEntity.setModifiedDate(new Date());
			this.userRepo.save(userEntity);
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
	}

}
