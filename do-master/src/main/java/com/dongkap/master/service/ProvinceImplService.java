package com.dongkap.master.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.dongkap.common.service.CommonService;
import com.dongkap.dto.common.FilterDto;
import com.dongkap.dto.select.SelectDto;
import com.dongkap.dto.select.SelectResponseDto;
import com.dongkap.master.dao.ProvinceRepo;
import com.dongkap.master.dao.specification.ProvinceSpecification;
import com.dongkap.master.entity.ProvinceEntity;

@Service("provinceService")
public class ProvinceImplService extends CommonService {

	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ProvinceRepo provinceRepo;

	public SelectResponseDto getSelectProvince(FilterDto filter) throws Exception {
		Page<ProvinceEntity> province = provinceRepo.findAll(ProvinceSpecification.getSelect(filter.getKeyword()), page(filter.getOrder(), filter.getOffset(), filter.getLimit()));
		SelectResponseDto response = new SelectResponseDto();
		response.setTotalFiltered(Integer.toUnsignedLong(province.getContent().size()));
		response.setTotalRecord(provinceRepo.count(ProvinceSpecification.getSelect(filter.getKeyword())));
		province.getContent().forEach(value -> {
			response.getData().add(new SelectDto(value.getProvinceName(), value.getProvinceCode(), !value.isActive(), null));
		});
		return response;
	}

}
