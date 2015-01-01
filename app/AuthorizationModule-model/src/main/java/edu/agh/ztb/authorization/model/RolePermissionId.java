package edu.agh.ztb.authorization.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class RolePermissionId {
	@Column(name = "PERMISSION_ID", updatable = false)
	private Long permissionId;

	@Column(name = "ROLE_ID", updatable = false)
	private Long roleId;
}