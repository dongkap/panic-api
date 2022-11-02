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
public class FakeReportDto extends BaseAuditDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7550850975117219030L;
	private String id;
	private String fakeCode;
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
	private String status;
	private String userId;
	private String username;
	private String email;
	private String name;
	private ContactUserDto contact = new ContactUserDto();
	private PersonalInfoDto personalInfo = new PersonalInfoDto();

}