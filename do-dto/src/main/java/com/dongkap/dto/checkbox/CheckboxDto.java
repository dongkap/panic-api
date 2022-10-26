package com.dongkap.dto.checkbox;

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
public class CheckboxDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2907366297576163951L;
	private String id;
	private String name;
	private Boolean selected;
	private Boolean disabled;

}
