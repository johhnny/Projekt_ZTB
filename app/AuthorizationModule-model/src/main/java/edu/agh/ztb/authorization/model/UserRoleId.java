package edu.agh.ztb.authorization.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class UserRoleId {

	@Column(name = "USER_ID", updatable = false)
	private Long userId;

	@Column(name = "ROLE_ID", updatable = false)
	private Long roleId;
}