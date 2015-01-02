package edu.agh.ztb.authorization.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class RoleDto implements Serializable {

	private static final long serialVersionUID = -624754095469161326L;

	private String name;
	private List<PermissionDto> permissions;
}