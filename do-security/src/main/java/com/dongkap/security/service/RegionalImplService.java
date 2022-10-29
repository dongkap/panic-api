package com.dongkap.security.service;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongkap.common.exceptions.SystemErrorException;
import com.dongkap.common.service.CommonService;
import com.dongkap.common.utils.ErrorCode;
import com.dongkap.dto.common.CommonResponseDto;
import com.dongkap.dto.common.FilterDto;
import com.dongkap.dto.security.RegionalDto;
import com.dongkap.dto.select.SelectDto;
import com.dongkap.dto.select.SelectResponseDto;
import com.dongkap.security.dao.RegionalRepo;
import com.dongkap.security.dao.specification.RegionalSpecification;
import com.dongkap.security.entity.RegionalEntity;

@Service("regionalService")
public class RegionalImplService extends CommonService {

	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RegionalRepo regionalRepo;

	@Transactional
	public SelectResponseDto getSelect(FilterDto filter) throws Exception {
		Page<RegionalEntity> regional = regionalRepo.findAll(RegionalSpecification.getSelect(filter.getKeyword()), page(filter.getOrder(), filter.getOffset(), filter.getLimit()));
		final SelectResponseDto response = new SelectResponseDto();
		response.setTotalFiltered(Long.valueOf(regional.getContent().size()));
		response.setTotalRecord(regionalRepo.count(RegionalSpecification.getSelect(filter.getKeyword())));
		regional.getContent().forEach(value -> {
			response.getData().add(new SelectDto(value.getRegionalName(), value.getId(), !value.getActive(), null));
		});
		return response;
	}

	@Transactional
	public CommonResponseDto<RegionalDto> getDatatable(FilterDto filter) throws Exception {
		Page<RegionalEntity> regional = regionalRepo.findAll(RegionalSpecification.getDatatable(filter.getKeyword()), page(filter.getOrder(), filter.getOffset(), filter.getLimit()));
		final CommonResponseDto<RegionalDto> response = new CommonResponseDto<RegionalDto>();
		response.setTotalFiltered(Long.valueOf(regional.getContent().size()));
		response.setTotalRecord(regionalRepo.count(RegionalSpecification.getDatatable(filter.getKeyword())));
		regional.getContent().forEach(value -> {
			RegionalDto temp = new RegionalDto();
			temp.setRegionalName(value.getRegionalName());
			temp.setLatitude(value.getCoordinate().getX());
			temp.setLongitude(value.getCoordinate().getY());
			temp.setAdministrativeAreaShort(value.getAdministrativeAreaShort());
			temp.setAdministrativeAreaName(value.getAdministrativeAreaName());
			temp.setAddress(value.getAddress());
			temp.setTelpNumber(value.getTelpNumber());
			temp.setFaxNumber(value.getFaxNumber());
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
	
	@Transactional
	public void postRegional(RegionalDto request, String username) throws Exception {
		RegionalEntity regional = new RegionalEntity();
		if(request.getId() != null) {
			regional = this.regionalRepo.findById(request.getId()).orElse(new RegionalEntity());	
		}
		regional.setRegionalName(request.getRegionalName());
		regional.setAddress(request.getAddress());
		regional.setTelpNumber(request.getTelpNumber());
		regional.setFaxNumber(request.getFaxNumber());
		regionalRepo.saveAndFlush(regional);
	}

	public void deleteRegionals(List<String> regionalIds) throws Exception {
		List<RegionalEntity> corporates = regionalRepo.findByIdIn(regionalIds);
		try {
			regionalRepo.deleteInBatch(corporates);			
		} catch (DataIntegrityViolationException e) {
			throw new SystemErrorException(ErrorCode.ERR_SCR0009);
		} catch (ConstraintViolationException e) {
			throw new SystemErrorException(ErrorCode.ERR_SCR0009);
		}
	}

}
