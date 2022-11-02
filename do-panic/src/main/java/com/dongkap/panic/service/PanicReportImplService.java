package com.dongkap.panic.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dongkap.common.exceptions.SystemErrorException;
import com.dongkap.common.service.CommonService;
import com.dongkap.common.utils.DateUtil;
import com.dongkap.common.utils.ErrorCode;
import com.dongkap.common.utils.ParameterStatic;
import com.dongkap.dto.common.ApiBaseResponse;
import com.dongkap.dto.common.CommonResponseDto;
import com.dongkap.dto.common.FilterDto;
import com.dongkap.dto.file.FileMetadataDto;
import com.dongkap.dto.notification.PushNotificationDto;
import com.dongkap.dto.panic.FindNearestDto;
import com.dongkap.dto.panic.PanicReportDto;
import com.dongkap.dto.panic.RequestPanicReportDto;
import com.dongkap.dto.security.EmergencyContactDto;
import com.dongkap.dto.security.ProfileDto;
import com.dongkap.feign.service.EmployeeService;
import com.dongkap.feign.service.FileGenericService;
import com.dongkap.feign.service.ParameterI18nService;
import com.dongkap.feign.service.ProfileService;
import com.dongkap.feign.service.WebPushNotificationService;
import com.dongkap.panic.dao.DeviceRepo;
import com.dongkap.panic.dao.LocationRepo;
import com.dongkap.panic.dao.PanicDetailRepo;
import com.dongkap.panic.dao.PanicReportRepo;
import com.dongkap.panic.dao.specification.PanicReportSpecification;
import com.dongkap.panic.entity.DeviceEntity;
import com.dongkap.panic.entity.LocationEntity;
import com.dongkap.panic.entity.PanicDetailEntity;
import com.dongkap.panic.entity.PanicReportEntity;
import com.dongkap.panic.entity.Point;

@Service("panicReportService")
public class PanicReportImplService extends CommonService {

	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PanicReportRepo panicReportRepo;

	@Autowired
	private PanicDetailRepo panicDetailRepo;

	@Autowired
	private LocationRepo locationRepo;

	@Autowired
	private DeviceRepo deviceRepo;

	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	@Qualifier("fileEvidenceService")
	private FileGenericService fileEvidenceService;
	
    @Autowired
    private WebPushNotificationService webPushNotificationService;
	
	@Autowired
	private ParameterI18nService parameterI18nService;

	@Autowired
	private TokenStore tokenStore;
	
    @Value("${dongkap.notif.icon}")
    protected String iconNotify;
	
    @Value("${dongkap.notif.tag}")
    protected String tagNotify;

	private static final String DEFAULT_ROLE_ADMIN = "ROLE_ADMINISTRATOR";

	@Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = SystemErrorException.class)
	public ApiBaseResponse doPostPanicReport(RequestPanicReportDto dto, MultipartFile evidence, Authentication authentication, String p_locale) throws Exception {
		if (evidence != null && dto != null) {
			FileMetadataDto fileEvidence = new FileMetadataDto();
			Map<String, Object> additionalInfo = this.getAdditionalInformation(authentication);
			try {
				String pathName = additionalInfo.get("id").toString();
				fileEvidence = fileEvidenceService.putFile(pathName, evidence.getOriginalFilename(), evidence.getBytes());
			} catch (Exception e) {
				throw new SystemErrorException(ErrorCode.ERR_SCR0010);				
			}
			
			Point coordinate = new Point(dto.getLatitude(), dto.getLongitude());
			LocationEntity location = locationRepo.findByCoordinate(coordinate);
			if(location == null) {
				location = new LocationEntity();
				location.setCoordinate(coordinate);
				location.setFormattedAddress(dto.getFormattedAddress());
				location.setProvince(dto.getProvince());
				location.setCity(dto.getCity());
				location.setDistrict(dto.getDistrict());
				location = locationRepo.saveAndFlush(location);
			}
			
			DeviceEntity device = new DeviceEntity();
			device.setDeviceID(dto.getDeviceID());
			device.setDeviceName(dto.getDeviceName());
			device = deviceRepo.saveAndFlush(device);
			
			/**
			 * TODO If data report has been submit to fake report
			 */
			String panicCode = Base64.getEncoder().encodeToString((authentication.getName() + ":" + DateUtil.DATE.format(new Date())).getBytes());
			PanicReportEntity panic = panicReportRepo.findByPanicCode(panicCode);
			if(panic == null) {
				ProfileDto<?> profile = profileService.getProfile(authentication, p_locale);
				panic =  new PanicReportEntity();
				panic.setPanicCode(panicCode);
				panic.setUsername(profile.getUsername());
				panic.setName(profile.getName());
				panic.setPhoneNumber(profile.getContact().getPhoneNumber());
				panic.setGender(profile.getPersonalInfo().getGenderCode());
				panic.setAge(profile.getPersonalInfo().getAge());
				panic.setIdNumber(profile.getPersonalInfo().getIdNumber());
				panic.setMonth(ParameterStatic.MONTH_PARAMETER + DateUtil.getMonthNumber(p_locale, new Date()));
				panic.setYear(DateUtil.getYear(p_locale, new Date()));
			}
			panic.setLatestCoordinate(coordinate);
			panic.setLatestFormattedAddress(dto.getFormattedAddress());
			panic.setLatestProvince(dto.getProvince());
			panic.setLatestCity(dto.getCity());
			panic.setLatestDistrict(dto.getDistrict());
			panic.setLatestFileChecksum(fileEvidence.getChecksum());
			panic.setLatestDeviceID(dto.getDeviceID());
			panic.setLatestDeviceName(dto.getDeviceName());
			panic.setAdministrativeAreaShort(dto.getAdministrativeAreaShort());
			panic = panicReportRepo.saveAndFlush(panic);
			PanicDetailEntity panicDetail = new PanicDetailEntity();
			panicDetail.setFileChecksum(fileEvidence.getChecksum());
			panicDetail.setPanicReport(panic);
			panicDetail.setLocation(location);
			panicDetail.setDevice(device);
			panicDetailRepo.saveAndFlush(panicDetail);
			
			FindNearestDto findNearest = new FindNearestDto();
			findNearest.setAdministrativeAreaShort(dto.getAdministrativeAreaShort());
			findNearest.setLatitude(dto.getLatitude());
			findNearest.setLongitude(dto.getLongitude());
			List<String> to = new ArrayList<String>();
			to = this.employeeService.getEmployeeNearest(findNearest, p_locale);
			PushNotificationDto message = new PushNotificationDto();
			message.setTitle(authentication.getName());
			message.setBody(panic.getLatestFormattedAddress());
			message.setData(toPanicDto(panic, p_locale));
			message.setTag(tagNotify);
			message.setIcon(iconNotify);
			webPushNotificationService.broadcast(message, to);
			return null;
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
	}
	
	public List<PanicReportDto> getAllPanicReport(Authentication authentication, String p_locale) throws Exception {
		List<PanicReportEntity> panics = new ArrayList<PanicReportEntity>();
		Map<String, Object> additionalInfo = this.getAdditionalInformation(authentication);
		String authority = additionalInfo.get("authority").toString();
		Object administrativeAreaShort = additionalInfo.get("administrative_area_short");
		if(administrativeAreaShort == null && authority.equalsIgnoreCase(DEFAULT_ROLE_ADMIN)) {
			panics = panicReportRepo.findByActiveAndStatusNull(true);
		} else {
			panics = panicReportRepo.findByActiveAndStatusNullAndAdministrativeAreaShort(true, administrativeAreaShort.toString());
		}
		if(panics == null)
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
		List<PanicReportDto> response = new ArrayList<PanicReportDto>();
		panics.forEach(panic -> {
			try {
				PanicReportDto panicReportDto = toPanicDto(panic, p_locale);
				panicReportDto.setUsername(panic.getUsername());
				panicReportDto.setName(panic.getName());
				panicReportDto.getContact().setPhoneNumber(panic.getPhoneNumber());
				panicReportDto.getPersonalInfo().setIdNumber(panic.getIdNumber());
				panicReportDto.getPersonalInfo().setGender(panic.getGender());
				panicReportDto.getPersonalInfo().setAge(panic.getAge());
				response.add(panicReportDto);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return response;
	}
	
	public PanicReportDto getDetailInfoPanicReport(String id, Authentication authentication, String p_locale) throws Exception {
		if(id != null) {
			PanicReportEntity panic = panicReportRepo.findById(id).orElse(new PanicReportEntity());
			PanicReportDto panicReportDto = toPanicDto(panic, p_locale);
			panicReportDto = this.putProfileWithEmergencyContact(panicReportDto, panic.getUsername(), p_locale);
			panicReportDto.getContact().setPhoneNumber(panic.getPhoneNumber());
			panicReportDto.getPersonalInfo().setAge(panic.getAge());
			return panicReportDto;
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
	}

	public CommonResponseDto<PanicReportDto> getDatatablePanicReport(Authentication authentication, FilterDto filter, String locale) throws Exception {
		Map<String, Object> additionalInfo = this.getAdditionalInformation(authentication);
		String authority = additionalInfo.get("authority").toString();
		Object administrativeAreaShort = additionalInfo.get("administrative_area_short");
		if(administrativeAreaShort != null && !authority.equalsIgnoreCase(DEFAULT_ROLE_ADMIN)) {
	    	filter.getKeyword().put("administrativeAreaShort", administrativeAreaShort.toString());	
		}
		Page<PanicReportEntity> param = panicReportRepo.findAll(PanicReportSpecification.getDatatable(filter.getKeyword()), page(filter.getOrder(), filter.getOffset(), filter.getLimit()));
		CommonResponseDto<PanicReportDto> response = new CommonResponseDto<PanicReportDto>();
		response.setTotalFiltered(Integer.toUnsignedLong(param.getContent().size()));
		response.setTotalRecord(panicReportRepo.count(PanicReportSpecification.getDatatable(filter.getKeyword())));
		param.getContent().forEach(panic -> {
			try {
				PanicReportDto panicReportDto = toPanicDto(panic, locale);
				panicReportDto.setUsername(panic.getUsername());
				panicReportDto.setName(panic.getName());
				panicReportDto.getContact().setPhoneNumber(panic.getPhoneNumber());
				panicReportDto.getPersonalInfo().setIdNumber(panic.getIdNumber());
				panicReportDto.getPersonalInfo().setGenderCode(panic.getGender());
				panicReportDto.getPersonalInfo().setAge(panic.getAge());
				response.getData().add(panicReportDto);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return response;
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = SystemErrorException.class)
	public ApiBaseResponse doProcessPanicReport(Map<String, Object> dto, Authentication authentication, String p_locale) throws Exception {
		if (dto != null) {
			PanicReportEntity panic = panicReportRepo.findById(dto.get("id").toString()).orElse(null);
			if(panic != null) {
				panic.setEmergencyCategory(dto.get("emergencyCategory").toString());
				panic.setStatus(dto.get("status").toString());
				panic = panicReportRepo.saveAndFlush(panic);
				return null;
			} else
				throw new SystemErrorException(ErrorCode.ERR_SYS0404);
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
	}

	public Map<String, Object> getAdditionalInformation(Authentication auth) {
	    OAuth2AuthenticationDetails auth2AuthenticationDetails = (OAuth2AuthenticationDetails) auth.getDetails();
	    return tokenStore.readAccessToken(auth2AuthenticationDetails.getTokenValue()).getAdditionalInformation();
	}
	
	private PanicReportDto toPanicDto(PanicReportEntity panic, String p_locale) throws Exception {
		PanicReportDto response = new PanicReportDto();
		response.setId(panic.getId());
		response.setPanicCode(panic.getPanicCode());
		response.setMonth(DateUtil.getMonthName(p_locale, panic.getMonth()));
		response.setYear(panic.getYear());
		response.setLatestLatitude(panic.getLatestCoordinate().getX());
		response.setLatestLongitude(panic.getLatestCoordinate().getY());
		response.setLatestFormattedAddress(panic.getLatestFormattedAddress());
		response.setLatestProvince(panic.getLatestProvince());
		response.setLatestCity(panic.getLatestCity());
		response.setLatestDistrict(panic.getLatestDistrict());
		response.setLatestFileChecksum(panic.getLatestFileChecksum());
		response.setLatestDeviceID(panic.getLatestDeviceID());
		response.setLatestDeviceName(panic.getLatestDeviceName());
		response.setAdministrativeAreaShort(panic.getAdministrativeAreaShort());
		response.setEmergencyCategoryCode(panic.getEmergencyCategory());
		Map<String, Object> temp = new HashMap<String, Object>();
		if(panic.getEmergencyCategory() != null) {
			temp.put("parameterCode", panic.getEmergencyCategory());
			try {
				response.setEmergencyCategory(parameterI18nService.getParameter(temp, p_locale).getParameterValue());	
			} catch (Exception e) {}
		}
		response.setStatusCode(panic.getStatus());
		if(panic.getStatus() != null) {
			temp.put("parameterCode", panic.getStatus());
			response.setStatus(parameterI18nService.getParameter(temp, p_locale).getParameterValue());
		}
		response.setActive(panic.isActive());
		response.setVersion(panic.getVersion());
		response.setCreatedDate(panic.getCreatedDate());
		response.setCreatedBy(panic.getCreatedBy());
		response.setModifiedDate(panic.getModifiedDate());
		response.setModifiedBy(panic.getModifiedBy());
		return response;
	}
	
	private PanicReportDto putProfileWithEmergencyContact(PanicReportDto panicReportDto, String p_username, String p_locale) throws Exception {
		ProfileDto<EmergencyContactDto> profile = this.profileService.getProfileEmergency(p_username, p_locale);
		panicReportDto.setUserId(profile.getUserId());
		panicReportDto.setUsername(profile.getUsername());
		panicReportDto.setName(profile.getName());
		panicReportDto.setEmail(profile.getEmail());
		panicReportDto.setContact(profile.getContact());
		panicReportDto.setPersonalInfo(profile.getPersonalInfo());
		panicReportDto.setEmergencyContact(profile.getAdditionalInformation());
		return panicReportDto;
	}

}
