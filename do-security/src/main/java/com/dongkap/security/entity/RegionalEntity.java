package com.dongkap.security.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.dongkap.common.utils.SchemaDatabase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@ToString
@Entity
@Table(name = "sec_regional", schema = SchemaDatabase.SECURITY)
public class RegionalEntity extends BaseAuditEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1932022761237540822L;

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	@Column(name = "regional_uuid", nullable = false, unique = true)
	private String id;

	@Column(name = "regional_name", nullable = false)
	private String regionalName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "latitude", nullable = false)),
            @AttributeOverride(name = "y", column = @Column(name = "longitude", nullable = false))
    })
	private Point coordinate;

	@Column(name = "administrative_area_short")
	private String administrativeAreaShort;

	@Column(name = "administrative_area_name")
	private String administrativeAreaName;

	@Column(name = "address")
	private String address;

	@Column(name = "telp_number")
	private String telpNumber;

	@Column(name = "fax_number")
	private String faxNumber;

}