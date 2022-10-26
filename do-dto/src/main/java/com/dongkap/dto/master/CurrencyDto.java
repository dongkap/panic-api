package com.dongkap.dto.master;

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
public class CurrencyDto extends BaseAuditDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String currencyId;
	private String currencyCode;
	private String shortDesc;
	private String longDesc;
	private String baseEquivalent;
	private String glCode;
	private Boolean status;
}
