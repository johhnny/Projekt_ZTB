package edu.agh.ztb.authorization.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ROLE")
@Data
@NoArgsConstructor
public class Role {

	@Id
	@Column(name = "ID", updatable = false)
	@SequenceGenerator(name = "ROLE_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_SEQ")
	private Long id;

	@Column(name = "NAME", nullable = false, unique = true, length = 50)
	private String name;

	@Column(name = "DESCRIPTION", nullable = true, length = 250)
	private String description;

	@OneToMany(mappedBy = "roleId")
	private Set<RolePermission> rolePermissions;
}
