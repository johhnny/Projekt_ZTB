package edu.agh.ztb.authorization.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class UserDto implements Serializable {
	private static final long serialVersionUID = 8620528428688928293L;

	private String login;
	private String password;
	private String name;
	private String surname;
	private String email;
	private List<RoleDto> userRoles;
	private List<SessionDto> sessions;
}