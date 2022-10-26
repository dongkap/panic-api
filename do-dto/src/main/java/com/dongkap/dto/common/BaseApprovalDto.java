package com.dongkap.dto.common;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
public class BaseApprovalDto extends BaseAuditDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1183080114772374130L;
	private Boolean autoApproved;
	private String approved;
	private String approvedBy;
	@JsonFormat(pattern="dd/MM/yyyy")
	protected Date approvedDate;

	@JsonProperty("approved")
	public String getApproved() {
		return this.approved;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}

	@JsonIgnore
	public void setApproved(Boolean approved) {
		if(approved)
			this.approved = "Approved";
		else
			this.approved = "Not Approved";
	}

}
