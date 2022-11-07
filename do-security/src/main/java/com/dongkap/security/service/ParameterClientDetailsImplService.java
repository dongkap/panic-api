package com.dongkap.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongkap.common.exceptions.SystemErrorException;
import com.dongkap.common.pattern.PatternGlobal;
import com.dongkap.common.security.AESEncrypt;
import com.dongkap.common.service.CommonService;
import com.dongkap.common.utils.ErrorCode;
import com.dongkap.dto.common.ApiBaseResponse;
import com.dongkap.dto.common.CommonResponseDto;
import com.dongkap.dto.common.FilterDto;
import com.dongkap.dto.security.ClientDetailsDto;
import com.dongkap.security.dao.ClientDetailsRepo;
import com.dongkap.security.dao.specification.ClientDetailsSpecification;
import com.dongkap.security.entity.ClientDetailsEntity;

@Service("parameterClientDetailsService")
public class ParameterClientDetailsImplService extends CommonService {

	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ClientDetailsRepo clientDetailsRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Value("${dongkap.signature.aes.secret-key}")
	private String secretKey;

	public CommonResponseDto<ClientDetailsDto> getDatatable(FilterDto filter) throws Exception {
		Page<ClientDetailsEntity> clientDetails = clientDetailsRepo.findAll(ClientDetailsSpecification.getDatatable(filter.getKeyword()), page(filter.getOrder(), filter.getOffset(), filter.getLimit()));
		CommonResponseDto<ClientDetailsDto> response = new CommonResponseDto<ClientDetailsDto>();
		response.setTotalFiltered(Integer.toUnsignedLong(clientDetails.getContent().size()));
		response.setTotalRecord(clientDetailsRepo.count(ClientDetailsSpecification.getDatatable(filter.getKeyword())));
		clientDetails.getContent().forEach(value -> {
			ClientDetailsDto temp = new ClientDetailsDto();
			temp.setClientId(value.getClientId());
			temp.setResourceIds(value.getResourceIds());
			temp.setAuthorities(value.getAuthorities());
			temp.setAccessTokenValidity(value.getAccessTokenValidity());
			temp.setRefreshTokenValidity(value.getRefreshTokenValidity());
			temp.setAuthorizedGrantTypes(value.getAuthorizedGrantTypes());
			temp.setRegisteredRedirectUri(value.getRegisteredRedirectUri());
			temp.setScope(value.getScope());
			temp.setAutoApprove(value.getAutoApprove());
			temp.setAdditionalInformation(value.getAdditionalInformation());
			response.getData().add(temp);
		});
		return response;
	}

	public ClientDetailsDto getClientDetails(String param) throws Exception {
		ClientDetailsEntity clientDetails = clientDetailsRepo.findByClientId(param);
		ClientDetailsDto response = new ClientDetailsDto();
		response.setClientId(clientDetails.getClientId());
		response.setResourceIds(clientDetails.getResourceIds());
		response.setAuthorities(clientDetails.getAuthorities());
		response.setAccessTokenValidity(clientDetails.getAccessTokenValidity());
		response.setRefreshTokenValidity(clientDetails.getRefreshTokenValidity());
		response.setAuthorizedGrantTypes(clientDetails.getAuthorizedGrantTypes());
		response.setRegisteredRedirectUri(clientDetails.getRegisteredRedirectUri());
		response.setScope(clientDetails.getScope());
		response.setAutoApprove(clientDetails.getAutoApprove());
		response.setAdditionalInformation(clientDetails.getAdditionalInformation());
		return response;
	}

	@Transactional
	public ApiBaseResponse doPostClientDetails(ClientDetailsDto p_dto) throws Exception {
		ClientDetailsEntity clientDetails = clientDetailsRepo.findByClientId(p_dto.getClientId());
		if(clientDetails == null) {
			clientDetails = new ClientDetailsEntity();
			clientDetails.setClientId(p_dto.getClientId());
			if(p_dto.getClientSecret() == null) {
				throw new SystemErrorException(ErrorCode.ERR_SCR0002);
			}
		}
		if(p_dto.getClientSecret() != null) {	
			String password = AESEncrypt.decrypt(this.secretKey, p_dto.getClientSecret());
			if (password.matches(PatternGlobal.PASSWORD_MEDIUM.getRegex())) {
				clientDetails.setClientSecret(this.passwordEncoder.encode((String)password));	
			} else {
				throw new SystemErrorException(ErrorCode.ERR_SCR0005);
			}
		}
		clientDetails.setResourceIds(p_dto.getResourceIds());
		clientDetails.setAuthorities(p_dto.getAuthorities());
		clientDetails.setAccessTokenValidity(p_dto.getAccessTokenValidity());
		clientDetails.setRefreshTokenValidity(p_dto.getRefreshTokenValidity());
		clientDetails.setAuthorizedGrantTypes(p_dto.getAuthorizedGrantTypes());
		clientDetails.setRegisteredRedirectUri(p_dto.getRegisteredRedirectUri());
		clientDetails.setScope(p_dto.getScope());
		clientDetails.setAutoApprove(p_dto.getAutoApprove());
		clientDetails.setAdditionalInformation(p_dto.getAdditionalInformation());			
		return null;
	}

}
