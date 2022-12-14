package com.dongkap.security.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@EqualsAndHashCode(callSuper=false, exclude={"user"})
@ToString(exclude={"user"})
@Entity
@Table(name = "sec_personal_info", schema = SchemaDatabase.SECURITY)
public class PersonalInfoEntity extends BaseAuditEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2442773369159964802L;
	
	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
    @Column(name = "personal_info_uuid", nullable = false, unique=true)
	private String id;

	@Column(name = "id_number", nullable = true)
	private String idNumber;
	
	@Column(name = "gender", nullable = true)
	private String gender;
	
	@Column(name = "place_of_birth", nullable = true)
	private String placeOfBirth;
	
	@Column(name = "date_of_birth", nullable = true)
	private Date dateOfBirth;

	@Column(name = "image", nullable = true)
	private String image;

	@OneToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_uuid", nullable = false, updatable = false)
	private UserEntity user;

}