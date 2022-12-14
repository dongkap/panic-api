package com.dongkap.security.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dongkap.common.aspect.ResponseSuccess;
import com.dongkap.common.exceptions.BaseControllerException;
import com.dongkap.dto.common.ApiBaseResponse;
import com.dongkap.common.utils.SuccessCode;
import com.dongkap.dto.security.SignUpDto;
import com.dongkap.security.service.UserImplService;

@RestController
public class SignUpController extends BaseControllerException {

	@Autowired
	private UserImplService userService;

	@ResponseSuccess(SuccessCode.OK_DEFAULT)
	@RequestMapping(value = "/oauth/signup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiBaseResponse> doSignUp(@RequestHeader(name = HttpHeaders.ACCEPT_LANGUAGE, required = false) String locale,
			@RequestBody(required = true) SignUpDto p_dto) throws Exception {
		return new ResponseEntity<ApiBaseResponse>(userService.doSignUp(p_dto, locale), HttpStatus.OK);
	}
	
}
