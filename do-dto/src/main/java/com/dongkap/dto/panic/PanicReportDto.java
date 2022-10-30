package com.dongkap.dto.panic;

import com.dongkap.dto.common.BaseAuditDto;
import com.dongkap.dto.security.ContactUserDto;
import com.dongkap.dto.security.PersonalInfoDto;

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
public class PanicReportDto extends BaseAuditDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2442773369159964802L;
	private String id;
	private String panicCode;
	private String month;
	private Integer year;
	private Double latestLatitude;
	private Double latestLongitude;
	private String latestFormattedAddress;
	private String latestProvince;
	private String latestCity;
	private String latestDistrict;
	private String latestFileChecksum;
	private String latestDeviceID;
	private String latestDeviceName;
	private String administrativeAreaShort;
	private String emergencyCategory;
	private String emergencyCategoryCode;
	private String status;
	private String statusCode;
	private String username;
	private String name;
	private String email;
	private ContactUserDto contact = new ContactUserDto();
	private PersonalInfoDto personalInfo = new PersonalInfoDto();

}