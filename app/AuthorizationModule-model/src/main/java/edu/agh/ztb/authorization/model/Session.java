package edu.agh.ztb.authorization.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "SESSION")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Session implements Serializable {

	private static final long serialVersionUID = -9069769199346895369L;

	@Id
	@Column(name = "ID", updatable = false)
	@SequenceGenerator(name = "SESSION_SEQ", sequenceName = "SESSION_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SESSION_SEQ")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
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