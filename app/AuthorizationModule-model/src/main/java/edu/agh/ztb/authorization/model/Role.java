package edu.agh.ztb.authorization.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ROLE")
@Getter
@Setter
@ToString(exclude = {"rolePermissions"})
@EqualsAndHashCode(exclude = {"rolePermissions"})
@NoArgsConstructor
public class Role implements Serializable {

	private static final long serialVersionUID = 5044154664900344853L;

	@Id
	@Column(name = "ID", updatable = false)
	@SequenceGenerator(name = "ROLE_SEQ", sequenceName = "ROLE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_SEQ")
	private Long id;

	@Column(name = "NAME", nullable = false, unique = true, length = 50)
	private String name;

	@Column(name = "DESCRIPTION", nullable = true, length = 250)
	private String description;

	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
	private Set<RolePermission> rolePermissions;
}
