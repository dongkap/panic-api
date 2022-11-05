package com.dongkap.security.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongkap.common.exceptions.SystemErrorException;
import com.dongkap.common.utils.ErrorCode;
import com.dongkap.dto.common.ApiBaseResponse;
import com.dongkap.dto.common.CommonResponseDto;
import com.dongkap.dto.common.FilterDto;

@Service("parameterClientDetailsService")
public class ParameterClientDetailsImplService {

	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JdbcClientDetailsService jdbcClientDetailService;
	
	public CommonResponseDto<ClientDetails> getListClientDetails(FilterDto filter) throws Exception {
        try {
            if(filter.getKeyword() == null)
                throw new SystemErrorException(ErrorCode.ERR_SYS0404);
            List<ClientDetails> jsonDatas = jdbcClientDetailService.listClientDetails();
            CommonResponseDto<ClientDetails> response = new CommonResponseDto<ClientDetails>();
            for(ClientDetails jsonData: jsonDatas) {
                response.getData().add(jsonData);
            }
            response.setTotalRecord(Integer.valueOf(jsonDatas.size()).longValue());
            response.setTotalFiltered(Integer.valueOf(jsonDatas.size()).longValue());
            return response;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
	}
	

	@Transactional
	public ApiBaseResponse doPostClientDetails(BaseClientDetails p_dto) throws Exception {
		this.jdbcClientDetailService.updateClientDetails(p_dto);
		return null;
	}

}
