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
@Table(name = "USERS")
@Getter
@Setter
@ToString(exclude = {"userRoles", "sessions"})
@EqualsAndHashCode(exclude = {"userRoles", "sessions"})
@NoArgsConstructor
public class User implements Serializable {

	private static final long serialVersionUID = 7981044368149180318L;

	@Id
	@Column(name = "ID", updatable = false)
	@SequenceGenerator(name = "USERS_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SEQ")
	private Long id;

	@Column(name = "LOGIN", nullable = false, length = 25)
	private String login;

	@Column(name = "PASSWORD", nullable = false, length = 64)
	private String password;

	@Column(name = "NAME", nullable = false, length = 50)
	private String name;

	@Column(name = "SURNAME", nullable = false, length = 100)
	private String surname;

	@Column(name = "EMAIL", nullable = true, length = 254)
	private String email;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private Set<UserRole> userRoles;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private Set<Session> sessions;
}