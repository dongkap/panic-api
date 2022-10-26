package com.dongkap.dto.select;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Select2DataDto {

	private String id = "";
	private String text = "";
	private Map<String, Object> query = new HashMap<>();
}
