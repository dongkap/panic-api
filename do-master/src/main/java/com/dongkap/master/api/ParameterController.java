package com.dongkap.master.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dongkap.common.aspect.ResponseSuccess;
import com.dongkap.common.exceptions.BaseControllerException;
import com.dongkap.common.utils.SuccessCode;
import com.dongkap.dto.common.ApiBaseResponse;
import com.dongkap.dto.common.CommonResponseDto;
import com.dongkap.dto.common.FilterDto;
import com.dongkap.dto.master.ParameterDto;
import com.dongkap.dto.master.ParameterRequestDto;
import com.dongkap.master.service.ParameterImplService;

@RestController
@RequestMapping("/api/master")
public class ParameterController extends BaseControllerException {

	@Autowired
	private ParameterImplService parameterService;

	@RequestMapping(value = "/vw/get/parameter/v.1/{param}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ParameterDto> getParameter(Authentication authentication,
			@PathVariable(required = true) String param) throws Exception {
		return new ResponseEntity<ParameterDto>(this.parameterService.getParameter(param), HttpStatus.OK);
	}

	@RequestMapping(value = "/vw/auth/datatable/parameter/v.1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommonResponseDto<ParameterDto>> getDatatableParameter(Authentication authentication,
			@RequestBody(required = true) FilterDto filter) throws Exception {
		return new ResponseEntity<CommonResponseDto<ParameterDto>>(this.parameterService.getDatatableParameter(filter), HttpStatus.OK);
	}
	
	@ResponseSuccess(SuccessCode.OK_SCR009)
	@RequestMapping(value = "/trx/auth/parameter/v.1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiBaseResponse> postParameter(Authentication authentication,
			@RequestBody(required = true) ParameterRequestDto data) throws Exception {
		String username = authentication.getName();
		this.parameterService.postParameter(data, username);
		return new ResponseEntity<ApiBaseResponse>(new ApiBaseResponse(), HttpStatus.OK);
	}
	
}
