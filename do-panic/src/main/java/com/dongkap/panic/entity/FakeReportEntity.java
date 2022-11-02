package com.dongkap.panic.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
@EqualsAndHashCode(callSuper=false, exclude={"fakeDetails"})
@ToString(exclude={"fakeDetails"})
@Entity
@Table(name = "fake_report", schema = SchemaDatabase.PANIC)
public class FakeReportEntity extends BaseAuditEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2442773369159964802L;

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
    @Column(name = "fake_uuid", nullable = false, unique=true)
	private String id;

	@Column(name = "fake_code", nullable = false, unique = true)
	private String fakeCode;

	@Column(name = "user_uuid", nullable = false)
	private String userId;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "fullname", nullable = false)
	private String name;
	
	@Column(name = "address", nullable = true)
	private String address;
	
	@Column(name = "gender", nullable = false)
	private String gender;

	@Column(name = "age", nullable = false)
	private Integer age;

	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@Column(name = "id_number", nullable = false)
	private String idNumber;

	@Column(name = "image", nullable = true)
	private String image;

	@Column(name = "month_report", nullable = false)
	private String month;

	@Column(name = "year_report", nullable = false)
	private Integer year;

	@Column(name = "administrative_area_short", nullable = true)
	private String administrativeAreaShort;

	@Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "latest_latitude", nullable = false)),
            @AttributeOverride(name = "y", column = @Column(name = "latest_longitude", nullable = false))
    })
	private Point latestCoordinate;

	@Column(name = "latest_formatted_address", nullable = true)
	private String latestFormattedAddress;

	@Column(name = "latest_province", nullable = true)
	private String latestProvince;

	@Column(name = "latest_city", nullable = true)
	private String latestCity;

	@Column(name = "latest_district", nullable = true)
	private String latestDistrict;

	@Column(name = "latest_file_checksum", nullable = true)
	private String latestFileChecksum;

	@Column(name = "latest_device_id", nullable = true)
	private String latestDeviceID;

	@Column(name = "latest_device_name", nullable = true)
	private String latestDeviceName;

	@Column(name = "emergency_category", nullable = true)
	private String emergencyCategory;

	@Column(name = "status", nullable = true)
	private String status;
	
	@OneToMany(mappedBy = "fakeReport", targetEntity = FakeDetailEntity.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	private Set<FakeDetailEntity> fakeDetails = new HashSet<FakeDetailEntity>();

}