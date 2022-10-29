package com.dongkap.dto.panic;

import java.io.Serializable;

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
public class FindNearestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3420295820447783272L;
	private String administrativeAreaShort;
	private Double latitude;
	private Double longitude;

}