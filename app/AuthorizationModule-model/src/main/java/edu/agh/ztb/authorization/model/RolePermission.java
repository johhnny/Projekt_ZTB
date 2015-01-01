package edu.agh.ztb.authorization.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ROLE_PERMISSION")
@Data
@NoArgsConstructor
public class RolePermission {

	@EmbeddedId
	private RolePermissionId id;

	@ManyToOne
	private Permission permission;

	@ManyToOne
	private Role role;
}
