package com.dongkap.security.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongkap.common.exceptions.SystemErrorException;
import com.dongkap.common.pattern.PatternGlobal;
import com.dongkap.common.service.CommonService;
import com.dongkap.common.utils.DateUtil;
import com.dongkap.common.utils.ErrorCode;
import com.dongkap.common.utils.RandomString;
import com.dongkap.dto.common.CommonResponseDto;
import com.dongkap.dto.common.FilterDto;
import com.dongkap.dto.panic.FindNearestDto;
import com.dongkap.dto.security.ContactUserDto;
import com.dongkap.dto.security.CorporateDto;
import com.dongkap.dto.security.EmployeeCreateDto;
import com.dongkap.dto.security.EmployeeDto;
import com.dongkap.dto.security.PersonalInfoDto;
import com.dongkap.feign.service.EmployeeService;
import com.dongkap.security.dao.ContactUserRepo;
import com.dongkap.security.dao.CorporateRepo;
import com.dongkap.security.dao.EmployeeRepo;
import com.dongkap.security.dao.OccupationRepo;
import com.dongkap.security.dao.PersonalInfoRepo;
import com.dongkap.security.dao.RegionalRepo;
import com.dongkap.security.dao.RoleRepo;
import com.dongkap.security.dao.UserRepo;
import com.dongkap.security.dao.specification.EmployeeSpecification;
import com.dongkap.security.entity.ContactUserEntity;
import com.dongkap.security.entity.CorporateEntity;
import com.dongkap.security.entity.EmployeeEntity;
import com.dongkap.security.entity.OccupationEntity;
import com.dongkap.security.entity.PersonalInfoEntity;
import com.dongkap.security.entity.RegionalEntity;
import com.dongkap.security.entity.RoleEntity;
import com.dongkap.security.entity.SettingsEntity;
import com.dongkap.security.entity.UserEntity;

@Service("employeeService")
public class EmployeeImplService extends CommonService implements EmployeeService {

	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private OccupationRepo occupationRepo;

	@Autowired
	private RegionalRepo regionalRepo;

	@Autowired
	private CorporateRepo corporateRepo;

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private RoleRepo roleRepo;
	
	@Autowired
	private ContactUserRepo contactRepo;
	
	@Autowired
	private PersonalInfoRepo personalInfoRepo;
	
	@Value("${dongkap.app-code.default}")
	private String appCode;

	@Value("${dongkap.locale}")
	private String localeCode;

	@Value("${dongkap.web.url.activate-account}")
	private String urlActivateAccount;
	
	private static final String DEFAULT_ROLE_EMPLOYEE = "ROLE_MONITORING";
	
	private static final String DEFAULT_ROLE_ADMIN = "ROLE_ADMINISTRATOR";

	@Override
	public List<String> getEmployeeNearest(FindNearestDto p_dto, String p_locale)
			throws Exception {
		List<RegionalEntity> regionalList = this.regionalRepo.findByAdministrativeAreaShort(p_dto.getAdministrativeAreaShort());
		final Map<String, Double> distanceMap = new HashMap<String, Double>();
		regionalList.forEach(regional ->{
			distanceMap.put(regional.getId(), this.distanceCoordinate(p_dto.getLatitude(), p_dto.getLongitude(), regional.getCoordinate().getX(), regional.getCoordinate().getY()));
		});
		RegionalEntity regional = new RegionalEntity();
		Double minDistance = Double.MAX_VALUE;
		for(String key : distanceMap.keySet()) {
		    if (distanceMap.get(key) < minDistance) {
		    	minDistance = distanceMap.get(key);
		    	regional = regionalList.stream()
		    	  .filter(reg -> reg.getId().equals(key))
		    	  .findAny()
		    	  .orElse(new RegionalEntity());
		    }
		}
		List<EmployeeEntity> employeeList = this.employeeRepo.findByRegional_Id(regional.getId());
		List<String> toResult = new ArrayList<String>();
		employeeList.forEach(value -> {
			toResult.add(value.getUser().getUsername());
		});
		return toResult;
	}

	@Override
	public List<String> getEmployeeNearestIncludeAdmin(FindNearestDto p_dto, String p_locale)
			throws Exception {
		final List<String> toResult = this.getEmployeeNearest(p_dto, p_locale);
		RoleEntity role = this.roleRepo.findByAuthority(DEFAULT_ROLE_ADMIN);
		role.getUsers().forEach(user -> {
			toResult.add(user.getUsername());
		});
		return toResult;
		
	}

	@Transactional
	public CorporateDto getCorporate(String username) throws Exception {
		EmployeeEntity employee = employeeRepo.findByUser_Username(username);
		if(employee == null) {
			throw new SystemErrorException(ErrorCode.ERR_SYS0001);
		}
		if (employee.getCorporate() == null) {
			throw new SystemErrorException(ErrorCode.ERR_SYS0001);
		}
		CorporateEntity corporate = employee.getCorporate();
		CorporateDto response = new CorporateDto();
		response.setId(corporate.getId());
		response.setCorporateCode(corporate.getCorporateCode());
		response.setCorporateName(corporate.getCorporateName());
		response.setCorporateNonExpired(corporate.isCorporateNonExpired());
		response.setEmail(corporate.getEmail());
		response.setAddress(corporate.getAddress());
		response.setTelpNumber(corporate.getTelpNumber());
		response.setFaxNumber(corporate.getFaxNumber());
		response.setActive(corporate.getActive());
		response.setVersion(corporate.getVersion());
		response.setCreatedDate(corporate.getCreatedDate());
		response.setCreatedBy(corporate.getCreatedBy());
		response.setModifiedDate(corporate.getModifiedDate());
		response.setModifiedBy(corporate.getModifiedBy());
		return response;
	}

	@Transactional
	public CommonResponseDto<EmployeeDto> getDatatable(Map<String, Object> additionalInfo, FilterDto filter) throws Exception {
		if(additionalInfo.get("corporate_code") == null) {
			throw new SystemErrorException(ErrorCode.ERR_SYS0001);
		}
		filter.getKeyword().put("corporateCode", additionalInfo.get("corporate_code"));
		Page<EmployeeEntity> employee = employeeRepo.findAll(EmployeeSpecification.getDatatable(filter.getKeyword()), page(filter.getOrder(), filter.getOffset(), filter.getLimit()));
		final CommonResponseDto<EmployeeDto> response = new CommonResponseDto<EmployeeDto>();
		response.setTotalFiltered(Long.valueOf(employee.getContent().size()));
		response.setTotalRecord(employeeRepo.count(EmployeeSpecification.getDatatable(filter.getKeyword())));
		employee.getContent().forEach(value -> {
			EmployeeDto temp = new EmployeeDto();
			temp.setId(value.getId());
			temp.setIdEmployee(value.getIdEmployee());
			temp.setLastEducation(value.getLastEducationLevel());
			temp.setIdCardimage(value.getIdCardImage());
			temp.setActive(value.getActive());
			temp.setVersion(value.getVersion());
			temp.setCreatedDate(value.getCreatedDate());
			temp.setCreatedBy(value.getCreatedBy());
			temp.setModifiedDate(value.getModifiedDate());
			temp.setModifiedBy(value.getModifiedBy());
			if(value.getOccupation() != null) {
				temp.setOccupationCode(value.getOccupation().getCode());
				temp.setOccupationName(value.getOccupation().getName());
			}
			if(value.getRegional()!= null) {
				temp.setRegionalId(value.getOccupation().getId());
				temp.setRegionalName(value.getOccupation().getName());
			}
			if(value.getCorporate()!= null) {
				temp.setCorporateCode(value.getCorporate().getCorporateCode());
				temp.setCorporateName(value.getCorporate().getCorporateName());
			}
			if(value.getUser() != null) {
				temp.setFullname(value.getUser().getFullname());
				temp.setEmail(value.getUser().getEmail());
				temp.setUsername(value.getUser().getUsername());
			}
			response.getData().add(temp);
		});
		return response;
	}

	@Transactional
	public void postEmployee(Map<String, Object> additionalInfo, EmployeeCreateDto request, String p_locale) throws Exception {
		if(additionalInfo.get("corporate_code") == null) {
			throw new SystemErrorException(ErrorCode.ERR_SYS0001);
		}
		request.setCorporateCode(additionalInfo.get("corporate_code").toString());
		UserEntity user = createUser(request);
		user = this.userRepo.saveAndFlush(user);
		ContactUserEntity contact = createContact(request.getContact(), user);
		contact = this.contactRepo.saveAndFlush(contact);
		PersonalInfoEntity personalInfo = createPersonalInfo(request.getPersonalInfo(), user);
		personalInfo = this.personalInfoRepo.saveAndFlush(personalInfo);
		EmployeeEntity employee = createEmployee(request, user);
		employee = this.employeeRepo.saveAndFlush(employee);
	}

	private UserEntity createUser(EmployeeDto p_dto) throws Exception {
		UserEntity user = this.userRepo.loadByUsernameOrEmail(p_dto.getEmail().toLowerCase(), p_dto.getEmail().toLowerCase());
		if(user == null) {
			user = new UserEntity();
			user.setUsername(p_dto.getEmail());
			user.setEmail(p_dto.getEmail());
			user.setFullname(p_dto.getFullname());
			user.setPassword("N/A");
        	RoleEntity role = this.roleRepo.findByAuthority(DEFAULT_ROLE_EMPLOYEE);
        	user.getRoles().add(role);
			user.setAuthorityDefault(DEFAULT_ROLE_EMPLOYEE);
			user.setAppCode(appCode);
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MONTH, 1);
			user.setActivateExpired(cal.getTime());
			user.setActivateCode(new RandomString(6, new SecureRandom(), RandomString.digits).nextString());
			SettingsEntity settings = new SettingsEntity();
			settings.setUser(user);
			user.setSettings(settings);
			return user;
		} else
			throw new SystemErrorException(ErrorCode.ERR_SCR0010);
	}
	
	private ContactUserEntity createContact(ContactUserDto p_dto, UserEntity user) throws Exception {
		ContactUserEntity contactUser = new ContactUserEntity();
		if (p_dto.getAddress() == null) {
			throw new SystemErrorException(ErrorCode.ERR_SYS0405);
		}
		if (p_dto.getPhoneNumber() != null) {
			if (p_dto.getPhoneNumber().matches(PatternGlobal.PHONE_NUMBER.getRegex())) {
				contactUser.setPhoneNumber(p_dto.getPhoneNumber());	
			} else
				throw new SystemErrorException(ErrorCode.ERR_SCR0007A);
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0405);
		contactUser.setAddress(p_dto.getAddress());
		contactUser.setCountry(p_dto.getCountry());
		contactUser.setProvince(p_dto.getProvince());
		contactUser.setCity(p_dto.getCity());
		contactUser.setDistrict(p_dto.getDistrict());
		contactUser.setSubDistrict(p_dto.getSubDistrict());
		contactUser.setZipcode(p_dto.getZipcode());
		contactUser.setUser(user);
		return contactUser;
	}

	private PersonalInfoEntity createPersonalInfo(PersonalInfoDto p_dto, UserEntity user) throws Exception {
		PersonalInfoEntity personalInfo = new PersonalInfoEntity();
		if (p_dto.getIdNumber() == null ||
				p_dto.getGenderCode() == null ||
				p_dto.getPlaceOfBirth() == null ||
				p_dto.getDateOfBirth() == null) {
			throw new SystemErrorException(ErrorCode.ERR_SYS0405);
		}
		personalInfo.setIdNumber(p_dto.getIdNumber());
		personalInfo.setGender(p_dto.getGenderCode());
		personalInfo.setPlaceOfBirth(p_dto.getPlaceOfBirth());
		personalInfo.setDateOfBirth(DateUtil.DATE.parse(p_dto.getDateOfBirth()));
		personalInfo.setUser(user);
		return personalInfo;
	}
	
	private EmployeeEntity createEmployee(EmployeeDto p_to, UserEntity user) throws Exception {
		EmployeeEntity employeeParent = this.employeeRepo.findById(p_to.getParentId()).orElse(null);
		if(p_to.getOccupationCode() == null) {
			throw new SystemErrorException(ErrorCode.ERR_SYS0001);
		}
		OccupationEntity occupation = this.occupationRepo.findByCode(p_to.getOccupationCode());
		if(occupation == null) {
			throw new SystemErrorException(ErrorCode.ERR_SYS0001);
		}
		RegionalEntity regional = this.regionalRepo.findById(p_to.getRegionalId()).get();
		if(regional == null) {
			throw new SystemErrorException(ErrorCode.ERR_SYS0001);
		}
		CorporateEntity corporate = corporateRepo.findByCorporateCode(p_to.getCorporateCode());
		if(corporate == null) {
			throw new SystemErrorException(ErrorCode.ERR_SYS0001);
		}
		EmployeeEntity employee = new EmployeeEntity();
		employee.setIdEmployee(p_to.getIdEmployee());
		employee.setLastEducationLevel(p_to.getLastEducation());
		employee.setUser(user);
		employee.setParentEmployee(employeeParent);
		employee.setOccupation(occupation);
		employee.setRegional(regional);
		employee.setCorporate(corporate);
		return employee;
	}
	
	private Double distanceCoordinate(Double x1, Double y1, Double x2, Double y2) {
		Double earthRadius = Double.valueOf(6371);

		Double dLat = Math.toRadians(x2-x1);
		Double dLng = Math.toRadians(y2-y1);

		Double sindLat = Math.sin(dLat / 2);
		Double sindLng = Math.sin(dLng / 2);

		Double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
	        * Math.cos(Math.toRadians(x1)) * Math.cos(Math.toRadians(x2));

		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

		Double distance = earthRadius * c;

	    return distance;
	}

}
