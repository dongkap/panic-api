package com.dongkap.security.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import com.dongkap.common.utils.SchemaDatabase;
import com.dongkap.dto.security.RoleDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false, exclude={"users", "functions"})
@ToString(exclude={"users", "functions"})
@Entity
@Table(name = "sec_role", schema = SchemaDatabase.SECURITY)
public class RoleEntity extends BaseAuditEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5870155744883664118L;
	
	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	@Column(name = "role_uuid", nullable = false, unique = true)
	private String id;

	@Column(name = "role_name", unique = true)
	private String authority;

	@Column(name = "description")
	private String description;

	@ManyToMany(mappedBy = "roles", targetEntity = UserEntity.class, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	private Set<UserEntity> users = new HashSet<UserEntity>();
	
	@OneToMany(mappedBy = "role", targetEntity = FunctionEntity.class, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SELECT)
	private Set<FunctionEntity> functions = new HashSet<FunctionEntity>();

	@ManyToOne(targetEntity = SystemAuthorityEntity.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "sys_auth_uuid", nullable = false)
	private SystemAuthorityEntity sysAuth;

	@Transient
	public RoleDto dto(){
		RoleDto roleDto = new RoleDto();
		roleDto.setId(this.id);
		roleDto.setAuthority(this.authority);
		roleDto.setDescription(this.description);
		return roleDto;
	}
}