package com.dongkap.security.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
import com.dongkap.dto.security.RegionalDto;
import com.dongkap.dto.select.SelectResponseDto;
import com.dongkap.security.service.RegionalImplService;

@RestController
@RequestMapping("/api/security")
public class RegionalController extends BaseControllerException {
	
	@Autowired
	private RegionalImplService regionalService;

	@RequestMapping(value = "/vw/auth/datatable/regional/v.1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommonResponseDto<RegionalDto>> getDatatableRegional(Authentication authentication,
			@RequestBody(required = true) FilterDto filter) throws Exception {
		return new ResponseEntity<CommonResponseDto<RegionalDto>>(this.regionalService.getDatatable(filter), HttpStatus.OK);
	}

	@RequestMapping(value = "/vw/auth/select/regional/v.1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SelectResponseDto> getSelectRegional(Authentication authentication,
														 @RequestHeader(name = HttpHeaders.ACCEPT_LANGUAGE, required = false) String locale,
														 @RequestBody(required = true) FilterDto filter) throws Exception {
		return new ResponseEntity<SelectResponseDto>(this.regionalService.getSelect(filter), HttpStatus.OK);
	}

	@ResponseSuccess(SuccessCode.OK_DEFAULT)
	@RequestMapping(value = "/trx/auth/regional/v.1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiBaseResponse> postRegional(Authentication authentication,
			@RequestBody(required = true) RegionalDto data) throws Exception {
		String username = authentication.getName();
		this.regionalService.postRegional(data, username);
		return new ResponseEntity<ApiBaseResponse>(new ApiBaseResponse(), HttpStatus.OK);
	}

	@ResponseSuccess(SuccessCode.OK_DELETED)
	@RequestMapping(value = "/trx/auth/delete/regional/v.1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiBaseResponse> deleteRegional(Authentication authentication,
													  @RequestBody(required = true) List<String> datas) throws Exception {
		this.regionalService.deleteRegionals(datas);
		return new ResponseEntity<ApiBaseResponse>(new ApiBaseResponse(), HttpStatus.OK);
	}
	
}
