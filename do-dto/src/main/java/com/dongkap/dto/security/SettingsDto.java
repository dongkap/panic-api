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
public class SettingsDto extends BaseAuditDto {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2624389791249022903L;
	private String localeCode;
	private String localeIdentifier;
	private String localeIcon;
    private String theme;

}
