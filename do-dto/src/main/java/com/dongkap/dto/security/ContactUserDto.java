package com.dongkap.dto.security;

import com.dongkap.dto.common.BaseAuditDto;

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
public class ContactUserDto extends BaseAuditDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1742415621743889509L;
	private String phoneNumber;
	private String address;
	private String country;
	private String countryCode;
	private String province;
	private String provinceCode;
	private String city;
	private String cityCode;
	private String district;
	private String districtCode;
	private String subDistrict;
	private String subDistrictCode;
	private String zipcode;

}