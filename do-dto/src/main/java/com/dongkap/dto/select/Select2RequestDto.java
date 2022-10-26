package com.dongkap.dto.select;

import lombok.Data;

@Data
public class Select2RequestDto {

	private Select2DataDto search;
	private Integer page = 0;
	private Integer pageLimit = 10;
}
