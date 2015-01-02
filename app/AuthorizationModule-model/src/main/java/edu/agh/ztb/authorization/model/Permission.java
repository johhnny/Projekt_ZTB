package edu.agh.ztb.authorization.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "PERMISSION")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Permission implements Serializable {

	private static final long serialVersionUID = 8077840719051667298L;

	@Id
	@Column(name = "ID", updatable = false)
	@SequenceGenerator(name = "PERMISSION_SEQ", sequenceName = "PERMISSION_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERMISSION_SEQ")
	private Long id;

	@Column(name = "NAME", nullable = false, unique = true, length = 50)
	private String name;

	@Column(name = "DESCRIPTION", nullable = true, length = 250)
	private String description;
}