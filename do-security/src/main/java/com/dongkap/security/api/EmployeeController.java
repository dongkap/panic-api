package com.dongkap.security.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
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
import com.dongkap.dto.panic.FindNearestDto;
import com.dongkap.dto.security.EmployeeCreateDto;
import com.dongkap.dto.security.EmployeeDto;
import com.dongkap.security.service.EmployeeImplService;

@RestController
@RequestMapping("/api/security")
public class EmployeeController extends BaseControllerException {
	
	@Autowired
	private EmployeeImplService employeeService;

	@Autowired
	private TokenStore tokenStore;
	
	// Dangerous when searched by criminals
	@RequestMapping(value = "/vw/post/nearest/employee/v.1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> getEmployeeNearest(Authentication authentication,
			@RequestBody(required = true) FindNearestDto data,
			 @RequestHeader(name = HttpHeaders.ACCEPT_LANGUAGE, required = false) String locale) throws Exception {
		return new ResponseEntity<List<String>>(this.employeeService.getEmployeeNearest(data, locale), HttpStatus.OK);
	}

	@RequestMapping(value = "/vw/auth/datatable/employee/v.1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommonResponseDto<EmployeeDto>> getDatatableEmployee(Authentication authentication,
			@RequestBody(required = true) FilterDto filter) throws Exception {
		Map<String, Object> additionalInfo = this.getAdditionalInformation(authentication);
		return new ResponseEntity<CommonResponseDto<EmployeeDto>>(this.employeeService.getDatatable(additionalInfo, filter), HttpStatus.OK);
	}

	@ResponseSuccess(SuccessCode.OK_DEFAULT)
	@RequestMapping(value = "/trx/auth/add/employee/v.1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiBaseResponse> postEmployee(Authentication authentication,
			 @RequestHeader(name = HttpHeaders.ACCEPT_LANGUAGE, required = false) String locale,
			@RequestBody(required = true) EmployeeCreateDto data) throws Exception {
		Map<String, Object> additionalInfo = this.getAdditionalInformation(authentication);
		this.employeeService.postEmployee(additionalInfo, data, locale);
		return new ResponseEntity<ApiBaseResponse>(new ApiBaseResponse(), HttpStatus.OK);
	}

	public Map<String, Object> getAdditionalInformation(Authentication auth) {
	    OAuth2AuthenticationDetails auth2AuthenticationDetails = (OAuth2AuthenticationDetails) auth.getDetails();
	    return tokenStore.readAccessToken(auth2AuthenticationDetails.getTokenValue()).getAdditionalInformation();
	}
}
