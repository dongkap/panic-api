package com.dongkap.master.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.dongkap.common.exceptions.SystemErrorException;
import com.dongkap.common.service.CommonService;
import com.dongkap.common.utils.ErrorCode;
import com.dongkap.dto.common.CommonResponseDto;
import com.dongkap.dto.common.FilterDto;
import com.dongkap.dto.master.LocaleDto;
import com.dongkap.dto.select.SelectDto;
import com.dongkap.dto.select.SelectResponseDto;
import com.dongkap.master.dao.LocaleRepo;
import com.dongkap.master.dao.specification.LocaleSpecification;
import com.dongkap.master.entity.LocaleEntity;

@Service("localeService")
public class LocaleImplService extends CommonService {

	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private LocaleRepo localeRepo;

	public SelectResponseDto getSelectLocale(FilterDto filter) throws Exception {
		Page<LocaleEntity> locale = localeRepo.findAll(LocaleSpecification.getSelect(filter.getKeyword()), page(filter.getOrder(), filter.getOffset(), filter.getLimit()));
		SelectResponseDto response = new SelectResponseDto();
		response.setTotalFiltered(Integer.toUnsignedLong(locale.getContent().size()));
		response.setTotalRecord(localeRepo.count(LocaleSpecification.getSelect(filter.getKeyword())));
		locale.getContent().forEach(value -> {
			response.getData().add(new SelectDto(value.getIdentifier(), value.getLocaleCode(), !value.isActive(), value.getIcon()));
		});
		return response;
	}

	public SelectResponseDto getSelectAllLocale() throws Exception {
		List<LocaleEntity> locale = localeRepo.findAll();
		SelectResponseDto response = new SelectResponseDto();
		response.setTotalFiltered(Integer.toUnsignedLong(locale.size()));
		response.setTotalRecord(Integer.toUnsignedLong(locale.size()));
		locale.forEach(value -> {
			response.getData().add(new SelectDto(value.getIdentifier(), value.getLocaleCode(), !value.isActive(), value.getIcon()));
		});
		return response;
	}

	public List<LocaleDto> getAllLocale() throws Exception {
		List<LocaleEntity> locale = localeRepo.findAll();
		List<LocaleDto> response = new ArrayList<LocaleDto>();
		locale.forEach(value -> {
			LocaleDto temp = new LocaleDto();
			temp.setLocaleCode(value.getLocaleCode());
			temp.setIdentifier(value.getIdentifier());
			temp.setIcon(value.getIcon());
			temp.setLocaleDefault(value.isLocaleDefault());
			temp.setLocaleEnabled(value.isLocaleEnabled());
			temp.setActive(value.isActive());
			temp.setVersion(value.getVersion());
			temp.setCreatedBy(value.getCreatedBy());
			temp.setCreatedDate(value.getCreatedDate());
			temp.setModifiedBy(value.getModifiedBy());
			temp.setModifiedDate(value.getModifiedDate());
			response.add(temp);
		});
		return response;
	}

	public LocaleDto getLocale(String localeCode) throws Exception {
		LocaleEntity locale = localeRepo.findByLocaleCode(localeCode);
		LocaleDto response = new LocaleDto();
		response.setLocaleCode(locale.getLocaleCode());
		response.setIdentifier(locale.getIdentifier());
		response.setIcon(locale.getIcon());
		response.setLocaleDefault(locale.isLocaleDefault());
		response.setLocaleEnabled(locale.isLocaleEnabled());
		response.setActive(locale.isActive());
		response.setVersion(locale.getVersion());
		response.setCreatedDate(locale.getCreatedDate());
		response.setCreatedBy(locale.getCreatedBy());
		response.setModifiedDate(locale.getModifiedDate());
		response.setModifiedBy(locale.getModifiedBy());
		return response;
	}

	public CommonResponseDto<LocaleDto> getDatatableLocale(FilterDto filter) throws Exception {
		Page<LocaleEntity> locale = localeRepo.findAll(LocaleSpecification.getDatatable(filter.getKeyword()), page(filter.getOrder(), filter.getOffset(), filter.getLimit()));
		CommonResponseDto<LocaleDto> response = new CommonResponseDto<LocaleDto>();
		response.setTotalFiltered(Integer.toUnsignedLong(locale.getContent().size()));
		response.setTotalRecord(localeRepo.count(LocaleSpecification.getDatatable(filter.getKeyword())));
		locale.getContent().forEach(value -> {
			LocaleDto temp = new LocaleDto();
			temp.setLocaleCode(value.getLocaleCode());
			temp.setIdentifier(value.getIdentifier());
			temp.setIcon(value.getIcon());
			temp.setLocaleDefault(value.isLocaleDefault());
			temp.setLocaleEnabled(value.isLocaleEnabled());
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
	
	@Transactional
	public void postLocale(LocaleDto request, String username) throws Exception {
		if (request.getLocaleCode() != null && request.getIdentifier() != null) {
			if (request.isLocaleDefault()) {
				localeRepo.changeLocaleDefault();
				localeRepo.flush();
			}
			LocaleEntity locale = localeRepo.findByLocaleCode(request.getLocaleCode());
			if (locale == null) {
				locale = new LocaleEntity();
				locale.setLocaleCode(request.getLocaleCode());
				locale.setLocaleDefault(request.isLocaleDefault());
				locale.setCreatedBy(username);
				locale.setCreatedDate(new Date());
			} else {
				if (request.isLocaleDefault()) {
					locale.setLocaleCode(request.getLocaleCode());
				}
				locale.setModifiedBy(username);
				locale.setModifiedDate(new Date());				
			}
			locale.setLocaleEnabled(request.isLocaleEnabled());
			locale.setIdentifier(request.getIdentifier());
			locale.setIcon(request.getIcon());
			locale = localeRepo.saveAndFlush(locale);
		} else {
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
		}
	}

}
