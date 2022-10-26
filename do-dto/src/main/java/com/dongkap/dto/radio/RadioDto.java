package com.dongkap.dto.radio;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RadioDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2907366297576163951L;
	private String name;
	private String value;
	private Boolean selected;
	private Boolean disabled;

}
