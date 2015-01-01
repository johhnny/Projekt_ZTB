package edu.agh.ztb.authorization.model;

import java.util.List;
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
@Table(name = "USER")
@Data
@NoArgsConstructor
public class User {

	@Id
	@Column(name = "ID", updatable = false)
	@SequenceGenerator(name = "USER_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
	private Long id;

	@Column(name = "LOGIN", nullable = false, length = 25)
	private String login;

	@Column(name = "PASSWORD", nullable = false, length = 64)
	private String password;

	@Column(name = "NAME", nullable = false, length = 50)
	private String name;

	@Column(name = "SURNAME", nullable = false, length = 100)
	private String surname;

	@Column(name = "EMAIL", nullable = false, length = 254)
	private String email;

	@OneToMany(mappedBy = "userId")
	private Set<UserRole> userRoles;

	@OneToMany(mappedBy = "userId")
	private List<Session> sessions;
}