package com.dongkap.security.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dongkap.common.aspect.ResponseSuccess;
import com.dongkap.common.exceptions.BaseControllerException;
import com.dongkap.common.utils.SuccessCode;
import com.dongkap.dto.common.ApiBaseResponse;
import com.dongkap.dto.common.CommonResponseDto;
import com.dongkap.dto.common.FilterDto;
import com.dongkap.dto.security.ClientDetailsDto;
import com.dongkap.security.service.ParameterClientDetailsImplService;

@RestController
@RequestMapping("/api/security")
public class ClientDetailsController extends BaseControllerException {

	@Autowired
	private ParameterClientDetailsImplService parameterClientDetailsService;

	@RequestMapping(value = "/vw/auth/datatable/client-details/v.1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommonResponseDto<ClientDetailsDto>> getListClientDetails(Authentication authentication,
			@RequestBody(required = true) FilterDto filter) throws Exception {
		return new ResponseEntity<CommonResponseDto<ClientDetailsDto>>(parameterClientDetailsService.getDatatable(filter), HttpStatus.OK);
	}

	@RequestMapping(value = "/vw/auth/client-details/v.1/{param}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClientDetailsDto> getClientDetails(Authentication authentication,
			@PathVariable(required = true) String param) throws Exception {
		return new ResponseEntity<ClientDetailsDto>(parameterClientDetailsService.getClientDetails(param), HttpStatus.OK);
	}
	
	@ResponseSuccess(SuccessCode.OK_DEFAULT)
	@RequestMapping(value = "/trx/auth/client-details/v.1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiBaseResponse> putClientDetails(Authentication authentication,
												   @RequestHeader(name = HttpHeaders.ACCEPT_LANGUAGE, required = false) String locale,
												   @RequestBody(required = true) ClientDetailsDto p_dto) throws Exception {
		return new ResponseEntity<ApiBaseResponse>(parameterClientDetailsService.doPostClientDetails(p_dto), HttpStatus.OK);
	}
	
}
