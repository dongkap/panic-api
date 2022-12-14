package com.dongkap.master.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.dongkap.common.exceptions.SystemErrorException;
import com.dongkap.common.service.CommonService;
import com.dongkap.common.utils.ErrorCode;
import com.dongkap.dto.common.CommonResponseDto;
import com.dongkap.dto.common.FilterDto;
import com.dongkap.dto.master.ParameterGroupDto;
import com.dongkap.master.dao.ParameterGroupRepo;
import com.dongkap.master.dao.specification.ParameterGroupSpecification;
import com.dongkap.master.entity.ParameterGroupEntity;

@Service("parameterGroupService")
public class ParameterGroupImplService extends CommonService {

	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ParameterGroupRepo parameterGroupRepo;

	public ParameterGroupDto getParameterGroup(String parameterGroupCode) throws Exception {
		ParameterGroupEntity paramGroup = parameterGroupRepo.findByParameterGroupCode(parameterGroupCode);
		ParameterGroupDto response = new ParameterGroupDto();
		response.setParameterGroupCode(paramGroup.getParameterGroupCode());
		response.setParameterGroupName(paramGroup.getParameterGroupName());
		response.setParameterGroupType(paramGroup.getParameterGroupType());
		response.setActive(paramGroup.isActive());
		response.setVersion(paramGroup.getVersion());
		response.setCreatedDate(paramGroup.getCreatedDate());
		response.setCreatedBy(paramGroup.getCreatedBy());
		response.setModifiedDate(paramGroup.getModifiedDate());
		response.setModifiedBy(paramGroup.getModifiedBy());
		return response;
	}

	public CommonResponseDto<ParameterGroupDto> getDatatableParameterGroup(FilterDto filter) throws Exception {
		Page<ParameterGroupEntity> paramGroup = parameterGroupRepo.findAll(ParameterGroupSpecification.getDatatable(filter.getKeyword()), page(filter.getOrder(), filter.getOffset(), filter.getLimit()));
		CommonResponseDto<ParameterGroupDto> response = new CommonResponseDto<ParameterGroupDto>();
		response.setTotalFiltered(Integer.toUnsignedLong(paramGroup.getContent().size()));
		response.setTotalRecord(parameterGroupRepo.count(ParameterGroupSpecification.getDatatable(filter.getKeyword())));
		paramGroup.getContent().forEach(value -> {
			ParameterGroupDto temp = new ParameterGroupDto();
			temp.setParameterGroupCode(value.getParameterGroupCode());
			temp.setParameterGroupName(value.getParameterGroupName());
			temp.setParameterGroupType(value.getParameterGroupType());
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
	public void postParameterGroup(ParameterGroupDto request, String username) throws Exception {
		if (request.getParameterGroupName() != null && request.getParameterGroupCode() != null) {
			ParameterGroupEntity paramGroup = parameterGroupRepo.findByParameterGroupCode(request.getParameterGroupCode());
			if (paramGroup == null) {
				paramGroup = new ParameterGroupEntity();
				paramGroup.setParameterGroupCode(request.getParameterGroupCode());
				paramGroup.setParameterGroupType(request.getParameterGroupType());
				paramGroup.setCreatedBy(username);
				paramGroup.setCreatedDate(new Date());
			} else {
				paramGroup.setModifiedBy(username);
				paramGroup.setModifiedDate(new Date());				
			}
			paramGroup.setParameterGroupName(request.getParameterGroupName());
			parameterGroupRepo.saveAndFlush(paramGroup);
		} else {
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
		}
	}
	
	public void deleteParameterGroup(List<String> parameterGroupCodes) throws Exception {
		List<ParameterGroupEntity> parameterGroups = parameterGroupRepo.findByParameterGroupCodeIn(parameterGroupCodes);
		try {
			parameterGroupRepo.deleteInBatch(parameterGroups);			
		} catch (DataIntegrityViolationException e) {
			throw new SystemErrorException(ErrorCode.ERR_SCR0009);
		} catch (ConstraintViolationException e) {
			throw new SystemErrorException(ErrorCode.ERR_SCR0009);
		}
	}

}
