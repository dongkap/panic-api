package com.dongkap.panic.entity;

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
public class PanicStatisticAreaEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2442773369159964802L;

	private String area;
	private String emergency;
	private Long total;

}