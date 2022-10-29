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
public class RequestPanicReportDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1580501181371364781L;
	private Double latitude;
	private Double longitude;
	private String formattedAddress;
	private String administrativeAreaShort;
	private String province;
	private String city;
	private String district;
	private String deviceID;
	private String deviceName;

}