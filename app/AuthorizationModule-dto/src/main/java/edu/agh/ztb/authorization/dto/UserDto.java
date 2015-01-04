package edu.agh.ztb.authorization.dto;

import lombok.Data;
import lombok.experimental.Builder;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class UserDto implements Serializable {
	private static final long serialVersionUID = 8620528428688928293L;

	private String login;
	private String name;
	private String surname;
	private String email;
	private String currentToken;
	private List<RoleDto> userRoles;
}