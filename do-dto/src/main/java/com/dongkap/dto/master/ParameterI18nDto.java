package com.dongkap.dto.master;

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
public class ParameterI18nDto extends ParameterDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3113832689039097163L;
	private String parameterI18nId;
	private String parameterValue;
	private String locale;

}