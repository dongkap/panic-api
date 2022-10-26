package com.dongkap.common.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.dongkap.common.utils.ErrorCode;
import com.dongkap.dto.common.ApiBaseResponse;

public class BaseControllerException {

	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private WebResponseExceptionTranslator<OAuth2Exception> providerExceptionHandler = new DefaultWebResponseExceptionTranslator();
	
	@Autowired
	private MessageSource messageSource;
	
	@Value("${dongkap.locale}")
	private String locale;
	
	protected void rollback() {}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiBaseResponse> handleException(HttpServletRequest request, Exception exception) throws Exception {
		if (exception instanceof FeignAuthException) {
			throw exception;
		}
		LOGGER.error(exception.getMessage(), exception);
		
		Locale locale = Locale.getDefault();
		String acceptLanguage = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
		if(acceptLanguage != null)
			locale = Locale.forLanguageTag(acceptLanguage);
		Map<String, String> respStatusMessage = new HashMap<String, String>();
		respStatusMessage.put(ErrorCode.ERR_SYS0500.name(), this.errorDescriptionResponse(ErrorCode.ERR_SYS0500, locale));
		ApiBaseResponse baseResponse = new ApiBaseResponse();
		baseResponse.setRespStatusCode(ErrorCode.ERR_SYS0500.name());
		baseResponse.setRespStatusMessage(respStatusMessage);
		return new ResponseEntity<ApiBaseResponse>(baseResponse,
				ErrorCode.ERR_SYS0500.getStatus());
	}

	@ExceptionHandler(ClientRegistrationException.class)
	public ResponseEntity<ApiBaseResponse> handleClientRegistrationException(Exception e) throws Exception {
		BadClientCredentialsException exception = new BadClientCredentialsException();
		Map<String, String> respStatusMessage = new HashMap<String, String>();
		respStatusMessage.put(exception.getOAuth2ErrorCode(), exception.getMessage());
		ApiBaseResponse baseResponse = new ApiBaseResponse();
		baseResponse.setRespStatusCode(exception.getOAuth2ErrorCode());
		baseResponse.setRespStatusMessage(respStatusMessage);
		return new ResponseEntity<ApiBaseResponse>(baseResponse,
				HttpStatus.valueOf(exception.getHttpErrorCode()));
	}

	@ExceptionHandler(OAuth2Exception.class)
	public ResponseEntity<ApiBaseResponse> handleException(OAuth2Exception e) throws Exception {
		Map<String, String> respStatusMessage = new HashMap<String, String>();
		respStatusMessage.put(e.getOAuth2ErrorCode(), e.getMessage());
		ApiBaseResponse baseResponse = new ApiBaseResponse();
		baseResponse.setRespStatusCode(e.getOAuth2ErrorCode());
		baseResponse.setRespStatusMessage(respStatusMessage);
		return new ResponseEntity<ApiBaseResponse>(baseResponse,
				HttpStatus.valueOf(e.getHttpErrorCode()));
	}
	
	@ExceptionHandler(MissingServletRequestPartException.class)
	public ResponseEntity<ApiBaseResponse> handleMissingServletRequestPartException(HttpServletRequest request, MissingServletRequestPartException exception) {
		LOGGER.error(exception.getMessage(), exception);
		
		Locale locale = Locale.getDefault();
		String acceptLanguage = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
		if(acceptLanguage != null)
			locale = Locale.forLanguageTag(acceptLanguage);
		Map<String, String> respStatusMessage = new HashMap<String, String>();
		respStatusMessage.put(ErrorCode.ERR_SYS0404.name(), this.errorDescriptionResponse(ErrorCode.ERR_SYS0404, locale));
		ApiBaseResponse baseResponse = new ApiBaseResponse();
		baseResponse.setRespStatusCode(ErrorCode.ERR_SYS0404.name());
		baseResponse.setRespStatusMessage(respStatusMessage);
		return new ResponseEntity<ApiBaseResponse>(baseResponse,
				ErrorCode.ERR_SYS0404.getStatus());
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiBaseResponse> handleConstraintException(HttpServletRequest request, DataIntegrityViolationException exception) {
		stackTrace(exception);
		
		Locale locale = Locale.getDefault();
		String acceptLanguage = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
		if(acceptLanguage != null)
			locale = Locale.forLanguageTag(acceptLanguage);
		Map<String, String> respStatusMessage = new HashMap<String, String>();
		respStatusMessage.put(ErrorCode.ERR_SCR0010.name(), this.errorDescriptionResponse(ErrorCode.ERR_SCR0010, locale));
		ApiBaseResponse baseResponse = new ApiBaseResponse();
		baseResponse.setRespStatusCode(ErrorCode.ERR_SCR0010.name());
		baseResponse.setRespStatusMessage(respStatusMessage);
		return new ResponseEntity<ApiBaseResponse>(baseResponse,
				ErrorCode.ERR_SCR0010.getStatus());
	}
	
	@ExceptionHandler(NoSuchAlgorithmException.class)
	public ResponseEntity<ApiBaseResponse> handleEncryptException(HttpServletRequest request, NoSuchAlgorithmException exception) {
		stackTrace(exception);
		
		Locale locale = Locale.getDefault();
		String acceptLanguage = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
		if(acceptLanguage != null)
			locale = Locale.forLanguageTag(acceptLanguage);
		Map<String, String> respStatusMessage = new HashMap<String, String>();
		respStatusMessage.put(ErrorCode.ERR_SCR0004.name(), this.errorDescriptionResponse(ErrorCode.ERR_SCR0004, locale));
		ApiBaseResponse baseResponse = new ApiBaseResponse();
		baseResponse.setRespStatusCode(ErrorCode.ERR_SCR0004.name());
		baseResponse.setRespStatusMessage(respStatusMessage);
		return new ResponseEntity<ApiBaseResponse>(baseResponse,
				ErrorCode.ERR_SCR0004.getStatus());
	}

	@ExceptionHandler(FeignThrowException.class)
	public ResponseEntity<ApiBaseResponse> handleFeignThrowException(HttpServletRequest request, Exception exception) throws Exception {
		FeignThrowException e = (FeignThrowException) exception;
		return new ResponseEntity<ApiBaseResponse>((ApiBaseResponse) e.getResponse(), e.getStatus());
	}
	
	@ExceptionHandler(SystemErrorException.class)
	public ResponseEntity<ApiBaseResponse> handleSystemException(HttpServletRequest request, SystemErrorException exception) {
		Locale locale = Locale.getDefault();
		String acceptLanguage = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
		if(acceptLanguage != null)
			locale = Locale.forLanguageTag(acceptLanguage);	
		String errorCode = null;
		String errorDescription = null;
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		
		if (exception.getErrorCode() != null) {
			if (exception.getErrorCode().equals(ErrorCode.ERR_SYS0500))
				LOGGER.error(exception.getMessage(), exception);
			status = exception.getErrorCode().getStatus();
			errorCode = exception.getErrorCode().name();
			if(exception.getParams() != null)
				errorDescription = this.errorDescriptionResponse(errorCode, locale, exception.getParams());	
			else
				errorDescription = this.errorDescriptionResponse(errorCode, locale, exception.getParams());
		} else if (exception.getStatus() != null) {
			errorCode = exception.getStatus().name();
			errorDescription = exception.getStatus().getReasonPhrase();
		}
		Map<String, String> respStatusMessage = new HashMap<String, String>();
		respStatusMessage.put(errorCode, errorDescription);
		ApiBaseResponse baseResponse = new ApiBaseResponse();
		baseResponse.setRespStatusCode(errorCode);
		baseResponse.setRespStatusMessage(respStatusMessage);
		return new ResponseEntity<ApiBaseResponse>(baseResponse, status);
	}

	protected WebResponseExceptionTranslator<OAuth2Exception> getExceptionTranslator() {
		return providerExceptionHandler;
	}
	
	public String errorDescriptionResponse(ErrorCode errorCode, Locale locale) {
		if(locale == null)
			locale = Locale.forLanguageTag(this.locale);
		return messageSource.getMessage(errorCode.name(), null, locale);
	}
	
	public String errorDescriptionResponse(String errorCode, Locale locale, Object[] params) {
		if(locale == null)
			locale = Locale.forLanguageTag(this.locale);
		return messageSource.getMessage(errorCode, params, locale);
	}

	private String stackTrace(Exception exception) {
		StringWriter errors = new StringWriter();
		exception.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}

}
