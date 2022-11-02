package com.dongkap.dto.chart;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class AxisChartDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5356989006407136210L;
	private Set<String> data = new HashSet<String>();

}