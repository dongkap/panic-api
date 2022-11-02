package com.dongkap.panic.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.dongkap.common.exceptions.SystemErrorException;
import com.dongkap.common.utils.ErrorCode;
import com.dongkap.dto.chart.AxisChartDto;
import com.dongkap.dto.chart.CommonChartDto;
import com.dongkap.dto.chart.LegendChartDto;
import com.dongkap.dto.chart.SeriesChartDto;
import com.dongkap.dto.master.ParameterI18nDto;
import com.dongkap.feign.service.ParameterI18nService;
import com.dongkap.panic.dao.PanicReportRepo;
import com.dongkap.panic.entity.PanicStatisticAreaEntity;
import com.dongkap.panic.entity.PanicStatisticGenderEntity;
import com.dongkap.panic.utils.ParameterEmergency;

@Service("statisticsPanicReportService")
public class StatisticsPanicReportImplService {

	protected Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PanicReportRepo panicReportRepo;
	
	@Autowired
	private ParameterI18nService parameterI18nService;

	@Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = SystemErrorException.class)
	public CommonChartDto getStatisticsArea(Integer year, Authentication authentication, String p_locale) throws Exception {
		if (year != null) {
			List<PanicStatisticAreaEntity> panics = panicReportRepo.loadDataGroupProvinceByEmergencyCategory(year);
			if(panics != null) {
				List<String> parameterCodes = new ArrayList<String>(Arrays.asList(new String[] {
						ParameterEmergency.CATEGORY_EMERGENCY_FIRE,
						ParameterEmergency.CATEGORY_EMERGENCY_ABDUCTION,
						ParameterEmergency.CATEGORY_EMERGENCY_THEFT,
						ParameterEmergency.CATEGORY_EMERGENCY_HER,
						ParameterEmergency.CATEGORY_EMERGENCY_UNREST,
						ParameterEmergency.CATEGORY_EMERGENCY_HARASSMENT,
						ParameterEmergency.CATEGORY_EMERGENCY_THREAT,
						ParameterEmergency.CATEGORY_EMERGENCY_BULLYING,
						ParameterEmergency.CATEGORY_EMERGENCY_OTHERS
				}));
				List<ParameterI18nDto> emergencyCategories = parameterI18nService.getParameterCode(parameterCodes, p_locale);
				CommonChartDto chart = new CommonChartDto();
				LegendChartDto legend = new LegendChartDto();
				AxisChartDto axis = new AxisChartDto();
				axis.getData().addAll(panics.stream().filter((distinctByKey(PanicStatisticAreaEntity::getArea))).map(panic -> panic.getArea()).collect(Collectors.toList()));
				emergencyCategories.forEach(emergencyCategory -> {
					List<Long> data = new ArrayList<Long>();
					axis.getData().forEach(area -> {
						PanicStatisticAreaEntity panicData = panics.stream().filter(panic->panic.getEmergency().equalsIgnoreCase(emergencyCategory.getParameterCode()) && panic.getArea().equalsIgnoreCase(area)).findFirst().orElse(null);
						if(panicData != null ) { 
							data.add(panicData.getTotal());
						}
					});
					legend.getData().add(emergencyCategory.getParameterValue());
					SeriesChartDto series = new SeriesChartDto();
					series.setName(emergencyCategory.getParameterValue());
					series.getData().put(emergencyCategory.getParameterValue(), data);
					chart.getSeries().add(series);
				});
				chart.setLegend(legend);
				chart.setAxis(axis);
				return chart;
			} else
				throw new SystemErrorException(ErrorCode.ERR_SYS0404);
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = SystemErrorException.class)
	public CommonChartDto getAllStatisticsArea(Integer year, Authentication authentication, String p_locale) throws Exception {
		if (year != null) {
			List<Map<String, Object>> panics = panicReportRepo.loadDataGroupByProvince(year);
			if(panics != null) {
				CommonChartDto chart = new CommonChartDto();
				AxisChartDto axis = new AxisChartDto();
				SeriesChartDto series = new SeriesChartDto();
				LegendChartDto legend = new LegendChartDto();
				panics.forEach(panic -> {
					legend.getData().add(panic.get("area").toString());
					series.setName(panic.get("area").toString());
					series.getData().put(panic.get("area").toString(), panic.get("total"));
					axis.getData().add(panic.get("area").toString());
				});
				chart.setLegend(legend);
				chart.setAxis(axis);
				chart.getSeries().add(series);
				return chart;
			} else
				throw new SystemErrorException(ErrorCode.ERR_SYS0404);
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = SystemErrorException.class)
	public CommonChartDto getStatisticsGender(Integer year, Authentication authentication, String p_gender, String p_locale) throws Exception {
		if (year != null) {
			List<PanicStatisticGenderEntity> panics = panicReportRepo.loadDataGroupGenderByEmergencyCategory(year, p_gender);
			if(panics != null) {
				List<String> parameterCodes = new ArrayList<String>(Arrays.asList(new String[] {
						ParameterEmergency.CATEGORY_EMERGENCY_FIRE,
						ParameterEmergency.CATEGORY_EMERGENCY_ABDUCTION,
						ParameterEmergency.CATEGORY_EMERGENCY_THEFT,
						ParameterEmergency.CATEGORY_EMERGENCY_HER,
						ParameterEmergency.CATEGORY_EMERGENCY_UNREST,
						ParameterEmergency.CATEGORY_EMERGENCY_HARASSMENT,
						ParameterEmergency.CATEGORY_EMERGENCY_THREAT,
						ParameterEmergency.CATEGORY_EMERGENCY_BULLYING,
						ParameterEmergency.CATEGORY_EMERGENCY_OTHERS
				}));
				List<ParameterI18nDto> emergencyCategories = parameterI18nService.getParameterCode(parameterCodes, p_locale);
				CommonChartDto chart = new CommonChartDto();
				LegendChartDto legend = new LegendChartDto();
				AxisChartDto axis = new AxisChartDto();
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("parameterCode", p_gender);
				ParameterI18nDto parameterGender = parameterI18nService.getParameter(param, p_locale);
				axis.getData().add(parameterGender.getParameterValue());
				emergencyCategories.forEach(emergencyCategory -> {
					List<Long> data = new ArrayList<Long>();
					PanicStatisticGenderEntity panicData = panics.stream().filter(panic->panic.getEmergency().equalsIgnoreCase(emergencyCategory.getParameterCode()) && panic.getGender().equalsIgnoreCase(parameterGender.getParameterCode())).findFirst().orElse(null);
					if(panicData != null ) { 
						data.add(panicData.getTotal());
					}
					legend.getData().add(emergencyCategory.getParameterValue());
					SeriesChartDto series = new SeriesChartDto();
					series.setName(emergencyCategory.getParameterValue());
					series.getData().put(emergencyCategory.getParameterValue(), data);
					chart.getSeries().add(series);
				});
				chart.setLegend(legend);
				chart.setAxis(axis);
				return chart;
			} else
				throw new SystemErrorException(ErrorCode.ERR_SYS0404);
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = SystemErrorException.class)
	public CommonChartDto getAllStatisticsGender(Integer year, Authentication authentication, String p_locale) throws Exception {
		if (year != null) {
			List<Map<String, Object>> panics = panicReportRepo.loadDataGroupByGender(year);
			if(panics != null) {
				CommonChartDto chart = new CommonChartDto();
				LegendChartDto legend = new LegendChartDto();
				AxisChartDto axis = new AxisChartDto();
				SeriesChartDto series = new SeriesChartDto();
				series.setName("Gender");
				Map<String, Object> temp = new HashMap<String, Object>();
				panics.forEach(panic -> {
					temp.put("parameterCode", panic.get("gender"));
					try {
						String keyData = parameterI18nService.getParameter(temp, p_locale).getParameterValue();
						legend.getData().add(keyData);
						series.getData().put(keyData, panic.get("total"));
						axis.getData().add(keyData);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
				chart.setLegend(legend);
				chart.setAxis(axis);
				chart.getSeries().add(series);
				return chart;
			} else
				throw new SystemErrorException(ErrorCode.ERR_SYS0404);
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = SystemErrorException.class)
	public CommonChartDto getStatisticsEmergency(Integer year, Authentication authentication, String p_locale) throws Exception {
		if (year != null) {
			List<Map<String, Object>> panics = panicReportRepo.loadDataGroupByEmergency(year);
			if(panics != null) {
				CommonChartDto chart = new CommonChartDto();
				LegendChartDto legend = new LegendChartDto();
				AxisChartDto axis = new AxisChartDto();
				SeriesChartDto series = new SeriesChartDto();
				series.setName("Emergency");
				Map<String, Object> temp = new HashMap<String, Object>();
				for(Map<String, Object> panic: panics) {
					if(panic.get("emergency") != null) {
						temp.put("parameterCode", panic.get("emergency"));
						try {
							String keyData = parameterI18nService.getParameter(temp, p_locale).getParameterValue();
							legend.getData().add(keyData);
							series.getData().put(keyData, panic.get("total"));
							axis.getData().add(keyData);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				chart.setLegend(legend);
				chart.setAxis(axis);
				chart.getSeries().add(series);
				return chart;
			} else
				throw new SystemErrorException(ErrorCode.ERR_SYS0404);
		} else
			throw new SystemErrorException(ErrorCode.ERR_SYS0404);
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}

}
