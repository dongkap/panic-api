package com.dongkap.master.service;

import java.util.Date;
import java.util.Locale;

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
import com.dongkap.dto.common.CommonResponseDto;
import com.dongkap.dto.common.FilterDto;
import com.dongkap.dto.master.ParameterDto;
import com.dongkap.dto.master.ParameterRequestDto;
import com.dongkap.dto.select.SelectDto;
import com.dongkap.dto.select.SelectResponseDto;
import com.dongkap.master.dao.ParameterGroupRepo;
import com.dongkap.master.dao.ParameterRepo;
import com.dongkap.master.dao.specification.ParameterSpecification;
import com.dongkap.master.entity.ParameterEntity;
import com.dongkap.master.entity.ParameterGroupEntity;

@Service("parameterService")
public class ParameterImplService extends CommonService {

	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ParameterRepo parameterRepo;

	@Autowired
	private ParameterGroupRepo parameterGroupRepo;

	@Value("${dongkap.locale}")
	private String locale;

	public ParameterDto getParameter(String parameterCode) throws Exception {
		ParameterEntity parameter = parameterRepo.findByParameterCode(parameterCode);
		ParameterDto response = new ParameterDto();
		response.setParameterCode(parameter.getParameterCode());
		response.setParameterValue(parameter.getParameterValue());
		response.setParameterGroupCode(parameter.getParameterGroup().getParameterGroupCode());
		response.setParameterGroupName(parameter.getParameterGroup().getParameterGroupName());
		response.setParameterGroupType(parameter.getParameterGroup().getParameterGroupType());
		return response;
	}

	public CommonResponseDto<ParameterDto> getDatatableParameter(FilterDto filter) throws Exception {
		Page<ParameterEntity> param = parameterRepo.findAll(ParameterSpecification.getDatatable(filter.getKeyword()), page(filter.getOrder(), filter.getOffset(), filter.getLimit()));
		CommonResponseDto<ParameterDto> response = new CommonResponseDto<ParameterDto>();
		response.setTotalFiltered(Integer.toUnsignedLong(param.getContent().size()));
		response.setTotalRecord(parameterRepo.count(ParameterSpecification.getDatatable(filter.getKeyword())));
		param.getContent().forEach(value -> {
			ParameterDto temp = new ParameterDto();
			temp.setParameterCode(value.getParameterCode());
			temp.setParameterValue(value.getParameterValue());
			temp.setParameterGroupCode(value.getParameterGroup().getParameterGroupCode());
			temp.setParameterGroupName(value.getParameterGroup().getParameterGroupName());
			temp.setParameterGroupType(value.getParameterGroup().getParameterGroupType());
			temp.setActive(value.isActive());
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
		Page<ParameterEntity> parameter = parameterRepo.findAll(ParameterSpecification.getSelect(filter.getKeyword()), page(filter.getOrder(), filter.getOffset(), filter.getLimit()));
		SelectResponseDto response = new SelectResponseDto();
		response.setTotalFiltered(Integer.toUnsignedLong(parameter.getContent().size()));
		response.setTotalRecord(parameterRepo.count(ParameterSpecification.getSelect(filter.getKeyword())));
		parameter.getContent().forEach(value -> {
			value.getParameterI18n().forEach(data -> {
				response.getData().add(new SelectDto(data.getParameterValue(), value.getParameterCode(), !value.isActive(), null));
			});
		});
		return response;
	}
	
	@Transactional
	public void postParameter(ParameterRequestDto request, String username) throws Exception {
		if (request.getParameterValues() != null && request.getParameterCode() != null && request.getParameterGroupCode() != null) {
			ParameterGroupEntity paramGroup = parameterGroupRepo.findByParameterGroupCode(request.getParameterGroupCode());
			if (paramGroup != null) {
				ParameterEntity param = parameterRepo.findByParameterCode(request.getParameterCode());
				if (param == null) {
					param = new ParameterEntity();
					param.setParameterGroup(paramGroup);
					param.setParameterCode(request.getParameterCode());
					param.setParameterValue(request.getParameterValues().get("value"));
					param.setCreatedBy(username);
					param.setCreatedDate(new Date());
					param = parameterRepo.saveAndFlush(param);
				} else {
					param.setParameterValue(request.getParameterValues().get("value"));
					param.setModifiedBy(username);
					param.setModifiedDate(new Date());
				}
				param = parameterRepo.saveAndFlush(param);
			} else {
				throw new SystemErrorException(ErrorCode.ERR_SYS0404);
			}
		} else {
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
		}
	}

}
