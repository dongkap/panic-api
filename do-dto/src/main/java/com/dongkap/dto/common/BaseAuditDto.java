package com.dongkap.dto.common;

import java.io.Serializable;
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
@EqualsAndHashCode
@ToString
public class BaseAuditDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1183080114772374130L;
	protected int version;
	protected String active;
	@JsonFormat(pattern="dd/MM/yyyy")
	protected Date createdDate;
	protected String createdBy;
	@JsonFormat(pattern="dd/MM/yyyy")
	protected Date modifiedDate;
	protected String modifiedBy;

	@JsonProperty("active")
	public String getActive() {
		return this.active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	@JsonIgnore
	public void setActive(Boolean active) {
		if(active)
			this.active = "Active";
		else
			this.active = "Deactivated";
	}

}
