package com.dongkap.dto.select;

import com.dongkap.dto.common.CommonResponseDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=false)
@ToString
public class SelectResponseDto extends CommonResponseDto<SelectDto> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8158078360152299884L;

}
