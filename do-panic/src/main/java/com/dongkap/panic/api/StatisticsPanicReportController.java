package com.dongkap.panic.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dongkap.common.exceptions.BaseControllerException;
import com.dongkap.common.utils.ParameterStatic;
import com.dongkap.dto.chart.CommonChartDto;
import com.dongkap.panic.service.StatisticsPanicReportImplService;

@RestController
@RequestMapping("/api/panic")
public class StatisticsPanicReportController extends BaseControllerException {

	@Autowired
	private StatisticsPanicReportImplService statisticsPanicReportService;

	@RequestMapping(value = "/vw/auth/statistics-area/v.1/{year}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<CommonChartDto> getStatisticsAreaByEmergencyCategory(Authentication authentication,
			@PathVariable(required = true) String year,
			@RequestHeader(name = HttpHeaders.ACCEPT_LANGUAGE, required = false) String locale) throws Exception {
		return new ResponseEntity<CommonChartDto>(this.statisticsPanicReportService.getStatisticsArea(Integer.parseInt(year), authentication, locale), HttpStatus.OK);
	}

	@RequestMapping(value = "/vw/auth/statistics-area/all/v.1/{year}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<CommonChartDto> getAllStatisticsArea(Authentication authentication,
			@PathVariable(required = true) String year,
			@RequestHeader(name = HttpHeaders.ACCEPT_LANGUAGE, required = false) String locale) throws Exception {
		return new ResponseEntity<CommonChartDto>(this.statisticsPanicReportService.getAllStatisticsArea(Integer.parseInt(year), authentication, locale), HttpStatus.OK);
	}

	@RequestMapping(value = "/vw/auth/statistics-gender/male/v.1/{year}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<CommonChartDto> getStatisticsGenderMaleByEmergencyCategory(Authentication authentication,
			@PathVariable(required = true) String year,
			@RequestHeader(name = HttpHeaders.ACCEPT_LANGUAGE, required = false) String locale) throws Exception {
		return new ResponseEntity<CommonChartDto>(this.statisticsPanicReportService.getStatisticsGender(Integer.parseInt(year), authentication, ParameterStatic.GENDER_MALE, locale), HttpStatus.OK);
	}

	@RequestMapping(value = "/vw/auth/statistics-gender/female/v.1/{year}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<CommonChartDto> getStatisticsGenderFemaleByEmergencyCategory(Authentication authentication,
			@PathVariable(required = true) String year,
			@RequestHeader(name = HttpHeaders.ACCEPT_LANGUAGE, required = false) String locale) throws Exception {
		return new ResponseEntity<CommonChartDto>(this.statisticsPanicReportService.getStatisticsGender(Integer.parseInt(year), authentication, ParameterStatic.GENDER_FEMALE, locale), HttpStatus.OK);
	}

	@RequestMapping(value = "/vw/auth/statistics-gender/all/v.1/{year}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<CommonChartDto> getAllStatisticsGender(Authentication authentication,
			@PathVariable(required = true) String year,
			@RequestHeader(name = HttpHeaders.ACCEPT_LANGUAGE, required = false) String locale) throws Exception {
		return new ResponseEntity<CommonChartDto>(this.statisticsPanicReportService.getAllStatisticsGender(Integer.parseInt(year), authentication, locale), HttpStatus.OK);
	}

}
