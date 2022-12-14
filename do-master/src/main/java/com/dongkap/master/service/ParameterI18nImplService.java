package com.dongkap.master.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.dongkap.common.exceptions.SystemErrorException;
import com.dongkap.common.service.CommonService;
import com.dongkap.common.utils.ErrorCode;
import com.dongkap.dto.checkbox.CheckboxDto;
import com.dongkap.dto.common.CommonResponseDto;
import com.dongkap.dto.common.FilterDto;
import com.dongkap.dto.master.ParameterI18nDto;
import com.dongkap.dto.master.ParameterRequestDto;
import com.dongkap.dto.radio.RadioDto;
import com.dongkap.dto.select.SelectDto;
import com.dongkap.dto.select.SelectResponseDto;
import com.dongkap.feign.service.ParameterI18nService;
import com.dongkap.master.dao.ParameterGroupRepo;
import com.dongkap.master.dao.ParameterI18nRepo;
import com.dongkap.master.dao.ParameterRepo;
import com.dongkap.master.dao.specification.ParameterI18nSpecification;
import com.dongkap.master.entity.ParameterEntity;
import com.dongkap.master.entity.ParameterGroupEntity;
import com.dongkap.master.entity.ParameterI18nEntity;

@Service("parameterI18nService")
public class ParameterI18nImplService extends CommonService implements ParameterI18nService {

	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ParameterGroupRepo parameterGroupRepo;

	@Autowired
	private ParameterI18nRepo parameterI18nRepo;
	
	@Autowired
	private ParameterRepo parameterRepo;

	@Value("${dongkap.locale}")
	private String locale;

	public List<ParameterI18nDto> getParameterCode(Map<String, Object> filter) throws Exception {
		List<ParameterI18nEntity> param = parameterI18nRepo.findByParameter_ParameterCode(filter.get("parameterCode").toString());
		List<ParameterI18nDto> response = new ArrayList<ParameterI18nDto>();
		param.forEach(value -> {
			ParameterI18nDto temp = new ParameterI18nDto();
			temp.setParameterCode(value.getParameter().getParameterCode());
			temp.setParameterGroupCode(value.getParameter().getParameterGroup().getParameterGroupCode());
			temp.setParameterGroupName(value.getParameter().getParameterGroup().getParameterGroupName());
			temp.setParameterGroupType(value.getParameter().getParameterGroup().getParameterGroupType());
			temp.setParameterValue(value.getParameterValue());
			temp.setLocale(value.getLocaleCode());
			temp.setActive(value.isActive());
			temp.setVersion(value.getVersion());
			temp.setCreatedDate(value.getCreatedDate());
			temp.setCreatedBy(value.getCreatedBy());
			temp.setModifiedDate(value.getModifiedDate());
			temp.setModifiedBy(value.getModifiedBy());
			response.add(temp);
		});
		return response;
	}

	public List<ParameterI18nDto> getParameterI18n(String parameterCode) throws Exception {
		List<ParameterI18nEntity> param = parameterI18nRepo.findByParameter_ParameterCode(parameterCode);
		List<ParameterI18nDto> response = new ArrayList<ParameterI18nDto>();
		param.forEach(value -> {
			ParameterI18nDto temp = new ParameterI18nDto();
			temp.setParameterCode(value.getParameter().getParameterCode());
			temp.setParameterGroupCode(value.getParameter().getParameterGroup().getParameterGroupCode());
			temp.setParameterGroupName(value.getParameter().getParameterGroup().getParameterGroupName());
			temp.setParameterGroupType(value.getParameter().getParameterGroup().getParameterGroupType());
			temp.setParameterValue(value.getParameterValue());
			temp.setLocale(value.getLocaleCode());
			temp.setActive(value.isActive());
			temp.setVersion(value.getVersion());
			temp.setCreatedDate(value.getCreatedDate());
			temp.setCreatedBy(value.getCreatedBy());
			temp.setModifiedDate(value.getModifiedDate());
			temp.setModifiedBy(value.getModifiedBy());
			response.add(temp);
		});
		return response;
	}

	@Override
	public List<ParameterI18nDto> getParameterCode(List<String> parameterCodes, String locale) throws Exception {
		if(locale == null) {
			locale = this.locale;
		}
    	Locale i18n = Locale.forLanguageTag(locale);
    	if(i18n.getDisplayLanguage().isEmpty() || locale.contains(",")) {
    		locale = this.locale;
    	}
		List<ParameterI18nEntity> param = parameterI18nRepo.findByParameter_ParameterCodeInAndLocaleCode(parameterCodes, locale);
		List<ParameterI18nDto> response = new ArrayList<ParameterI18nDto>();
		param.forEach(value -> {
			ParameterI18nDto temp = new ParameterI18nDto();
			temp.setParameterCode(value.getParameter().getParameterCode());
			temp.setParameterGroupCode(value.getParameter().getParameterGroup().getParameterGroupCode());
			temp.setParameterGroupName(value.getParameter().getParameterGroup().getParameterGroupName());
			temp.setParameterGroupType(value.getParameter().getParameterGroup().getParameterGroupType());
			temp.setParameterValue(value.getParameterValue());
			temp.setLocale(value.getLocaleCode());
			temp.setActive(value.isActive());
			temp.setVersion(value.getVersion());
			temp.setCreatedDate(value.getCreatedDate());
			temp.setCreatedBy(value.getCreatedBy());
			temp.setModifiedDate(value.getModifiedDate());
			temp.setModifiedBy(value.getModifiedBy());
			response.add(temp);
		});
		return response;
	}
	
	@Transactional
	public void postParameterI18n(ParameterRequestDto request, String username) throws Exception {
		if (request.getParameterValues() != null && request.getParameterCode() != null && request.getParameterGroupCode() != null) {
			ParameterGroupEntity paramGroup = parameterGroupRepo.findByParameterGroupCode(request.getParameterGroupCode());
			if (paramGroup != null) {
				ParameterEntity param = parameterRepo.findByParameterCode(request.getParameterCode());
				if (param == null) {
					param = new ParameterEntity();
					param.setParameterGroup(paramGroup);
					param.setParameterCode(request.getParameterCode());
					param.setCreatedBy(username);
					param.setCreatedDate(new Date());
					Set<ParameterI18nEntity> parameterI18ns = new HashSet<ParameterI18nEntity>();
					for(String localeCode: request.getParameterValues().keySet()) {
							ParameterI18nEntity paramI18n = new ParameterI18nEntity();
							paramI18n.setLocaleCode(localeCode);
							paramI18n.setParameterValue(request.getParameterValues().get(localeCode));
							paramI18n.setParameter(param);
							paramI18n.setCreatedBy(username);
							paramI18n.setCreatedDate(new Date());
							parameterI18ns.add(paramI18n);
					}
					param.setParameterI18n(parameterI18ns);
					param = parameterRepo.saveAndFlush(param);
				} else {
					for(String localeCode: request.getParameterValues().keySet()) {
						if(request.getParameterValues().get(localeCode) != null) {
							ParameterI18nEntity paramI18n = parameterI18nRepo.findByParameter_ParameterCodeAndLocaleCode(request.getParameterCode(), localeCode);
							if (paramI18n == null) {
								paramI18n = new ParameterI18nEntity();
								paramI18n.setCreatedBy(username);
								paramI18n.setCreatedDate(new Date());
							} else {
								paramI18n.setModifiedBy(username);
								paramI18n.setModifiedDate(new Date());
							}
							paramI18n.setLocaleCode(localeCode);
							paramI18n.setParameterValue(request.getParameterValues().get(localeCode));
							paramI18n.setParameter(param);
							parameterI18nRepo.saveAndFlush(paramI18n);
						}
					}
				}
			} else {
				throw new SystemErrorException(ErrorCode.ERR_SYS0404);
			}
		} else {
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
		}
	}
	
	public ParameterI18nDto getParameter(Map<String, Object> param, String locale) throws Exception {
		if(locale == null) {
			locale = this.locale;
		}
    	Locale i18n = Locale.forLanguageTag(locale);
    	if(i18n.getDisplayLanguage().isEmpty() || locale.contains(",")) {
    		locale = this.locale;
    	}
		ParameterI18nEntity parameterI18n = parameterI18nRepo.findByParameter_ParameterCodeAndLocaleCode(param.get("parameterCode").toString(), locale);
		if(parameterI18n != null) {
			ParameterI18nDto temp = new ParameterI18nDto();
			temp.setParameterCode(parameterI18n.getParameter().getParameterCode());
			temp.setParameterGroupCode(parameterI18n.getParameter().getParameterGroup().getParameterGroupCode());
			temp.setParameterGroupName(parameterI18n.getParameter().getParameterGroup().getParameterGroupName());
			temp.setParameterGroupType(parameterI18n.getParameter().getParameterGroup().getParameterGroupType());
			temp.setParameterValue(parameterI18n.getParameterValue());
			temp.setLocale(parameterI18n.getLocaleCode());
			temp.setActive(parameterI18n.isActive());
			temp.setVersion(parameterI18n.getVersion());
			temp.setCreatedDate(parameterI18n.getCreatedDate());
			temp.setCreatedBy(parameterI18n.getCreatedBy());
			temp.setModifiedDate(parameterI18n.getModifiedDate());
			temp.setModifiedBy(parameterI18n.getModifiedBy());
			return temp;
		} else {
			return null;
		}
	}

	public CommonResponseDto<ParameterI18nDto> getDatatableParameterI18n(FilterDto filter) throws Exception {
		Page<ParameterI18nEntity> param = parameterI18nRepo.findAll(ParameterI18nSpecification.getDatatable(filter.getKeyword()), page(filter.getOrder(), filter.getOffset(), filter.getLimit()));
		final CommonResponseDto<ParameterI18nDto> response = new CommonResponseDto<ParameterI18nDto>();
		response.setTotalFiltered(Long.valueOf(param.getContent().size()));
		response.setTotalRecord(parameterI18nRepo.count(ParameterI18nSpecification.getDatatable(filter.getKeyword())));
		param.getContent().forEach(value -> {
			ParameterI18nDto temp = new ParameterI18nDto();
			temp.setParameterValue(value.getParameterValue());
			temp.setLocale(value.getLocaleCode());
			temp.setParameterCode(value.getParameter().getParameterCode());
			temp.setParameterGroupCode(value.getParameter().getParameterGroup().getParameterGroupCode());
			temp.setParameterGroupName(value.getParameter().getParameterGroup().getParameterGroupName());
			temp.setParameterGroupType(value.getParameter().getParameterGroup().getParameterGroupType());
			temp.setActive(value.getActive());
			temp.setVersion(value.getVersion());
			temp.setCreatedDate(value.getCreatedDate());
			temp.setCreatedBy(value.getCreatedBy());
			temp.setModifiedDate(value.getModifiedDate());
			temp.setModifiedBy(value.getModifiedBy());
			response.getData().add(temp);
		});
		return response;
	}

	public SelectResponseDto getSelect(FilterDto filter, String locale) throws Exception {
    	Locale i18n = Locale.forLanguageTag(locale);
    	if(i18n.getDisplayLanguage().isEmpty()) {
    		locale = this.locale;
    	}
    	filter.getKeyword().put("localeCode", locale);
		Page<ParameterI18nEntity> parameter = parameterI18nRepo.findAll(ParameterI18nSpecification.getSelect(filter.getKeyword()), page(filter.getOrder(), filter.getOffset(), filter.getLimit()));
		SelectResponseDto response = new SelectResponseDto();
		response.setTotalFiltered(Integer.toUnsignedLong(parameter.getContent().size()));
		response.setTotalRecord(parameterI18nRepo.count(ParameterI18nSpecification.getSelect(filter.getKeyword())));
		parameter.getContent().forEach(value -> {
			response.getData().add(new SelectDto(value.getParameterValue(), value.getParameter().getParameterCode(), !value.getParameter().isActive(), null));
		});
		return response;
	}

	public List<RadioDto> getRadio(FilterDto filter, String locale) throws Exception {
		if(locale == null) {
    		locale = this.locale;
		} else {
	    	final Locale i18n = Locale.forLanguageTag(locale);
	    	if(i18n.getDisplayLanguage().isEmpty()) {
	    		locale = this.locale;
	    	}	
		}
    	filter.getKeyword().put("localeCode", locale);
		Page<ParameterI18nEntity> parameter = parameterI18nRepo.findAll(ParameterI18nSpecification.getSelect(filter.getKeyword()), page(filter.getOrder(), filter.getOffset(), filter.getLimit()));
		final List<RadioDto> response = new ArrayList<RadioDto>();
		parameter.getContent().forEach(value -> {
			response.add(new RadioDto(value.getParameterValue(), value.getParameter().getParameterCode(), !value.getParameter().getActive(), null));
		});
		return response;
	}

	public List<CheckboxDto> getCheckbox(FilterDto filter, String locale) throws Exception {
		if(locale == null) {
    		locale = this.locale;
		} else {
	    	final Locale i18n = Locale.forLanguageTag(locale);
	    	if(i18n.getDisplayLanguage().isEmpty()) {
	    		locale = this.locale;
	    	}	
		}
    	filter.getKeyword().put("localeCode", locale);
		Page<ParameterI18nEntity> parameter = parameterI18nRepo.findAll(ParameterI18nSpecification.getSelect(filter.getKeyword()), page(filter.getOrder(), filter.getOffset(), filter.getLimit()));
		final List<CheckboxDto> response = new ArrayList<CheckboxDto>();
		parameter.getContent().forEach(value -> {
			response.add(new CheckboxDto(value.getParameter().getParameterCode(), value.getParameterValue(), !value.getParameter().getActive(), null));
		});
		return response;
	}

}
