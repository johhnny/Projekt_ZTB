package edu.agh.ztb.authorization.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SESSION")
@Data
@NoArgsConstructor
public class Session {

	@Id
	@Column(name = "ID", updatable = false)
	@SequenceGenerator(name = "SESSION_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SESSION_SEQ")
	private Long id;

	@Column(name = "USER_ID", updatable = false, nullable = false)
	private Long userId;

	@ManyToOne
	private User user;

	@Column(name = "TOKEN", updatable = false, nullable = false, unique = true, length = 36)
	private String token;

	@Temporal(TemporalType.TIME)
	@Column(name = "START_TIME", updatable = false, nullable = false)
	private Date startTime;

	@Temporal(TemporalType.TIME)
	@Column(name = "EXPIRATION_TIME", updatable = false, nullable = false)
	private Date expirationTime;
}