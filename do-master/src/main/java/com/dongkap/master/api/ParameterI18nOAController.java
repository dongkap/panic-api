package com.dongkap.master.api;

import java.util.Map;

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

import com.dongkap.common.exceptions.BaseControllerException;
import com.dongkap.dto.master.ParameterI18nDto;
import com.dongkap.master.service.ParameterI18nImplService;

@RestController
@RequestMapping("/oa")
public class ParameterI18nOAController extends BaseControllerException {

	@Autowired
	private ParameterI18nImplService parameterI18nService;

	@RequestMapping(value = "/vw/parameter-i18n/v.1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ParameterI18nDto> getParameterCodeOpenAPI(
			@RequestBody(required = true) Map<String, Object> param,
			@RequestHeader(name = HttpHeaders.ACCEPT_LANGUAGE, required = false) String locale) throws Exception {
		return new ResponseEntity<ParameterI18nDto>(this.parameterI18nService.getParameter(param, locale), HttpStatus.OK);
	}
	
}
