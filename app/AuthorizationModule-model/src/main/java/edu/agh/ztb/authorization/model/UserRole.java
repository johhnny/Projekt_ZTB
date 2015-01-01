package edu.agh.ztb.authorization.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER_ROLE")
@Data
@NoArgsConstructor
public class UserRole {

	@EmbeddedId
	private UserRoleId id;

	@ManyToOne
	private User user;

	@ManyToOne
	private Role role;
}