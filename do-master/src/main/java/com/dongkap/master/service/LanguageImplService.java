package com.dongkap.master.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.dongkap.common.service.CommonService;
import com.dongkap.dto.common.CommonResponseDto;
import com.dongkap.dto.common.FilterDto;
import com.dongkap.dto.master.LanguageDto;
import com.dongkap.dto.select.SelectDto;
import com.dongkap.dto.select.SelectResponseDto;
import com.dongkap.master.dao.LanguageRepo;
import com.dongkap.master.dao.specification.LanguageSpecification;
import com.dongkap.master.entity.LanguageEntity;

@Service("languageService")
public class LanguageImplService extends CommonService {

	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private LanguageRepo languageRepo;

	public SelectResponseDto getSelectLocale(FilterDto filter) throws Exception {
		Page<LanguageEntity> locale = languageRepo.findAll(LanguageSpecification.getSelect(filter.getKeyword()), page(filter.getOrder(), filter.getOffset(), filter.getLimit()));
		SelectResponseDto response = new SelectResponseDto();
		response.setTotalFiltered(Integer.toUnsignedLong(locale.getContent().size()));
		response.setTotalRecord(languageRepo.count(LanguageSpecification.getSelect(filter.getKeyword())));
		locale.getContent().forEach(value -> {
			response.getData().add(new SelectDto(value.getIdentifier(), value.getLanguageCode(), !value.isActive(), null));
		});
		return response;
	}

	public CommonResponseDto<LanguageDto> getDatatableLocale(FilterDto filter) throws Exception {
		Page<LanguageEntity> locale = languageRepo.findAll(LanguageSpecification.getDatatable(filter.getKeyword()), page(filter.getOrder(), filter.getOffset(), filter.getLimit()));
		CommonResponseDto<LanguageDto> response = new CommonResponseDto<LanguageDto>();
		response.setTotalFiltered(Integer.toUnsignedLong(locale.getContent().size()));
		response.setTotalRecord(languageRepo.count(LanguageSpecification.getDatatable(filter.getKeyword())));
		locale.getContent().forEach(value -> {
			LanguageDto temp = new LanguageDto();
			temp.setLanguageCode(value.getLanguageCode());
			temp.setIdentifier(value.getIdentifier());
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

}
