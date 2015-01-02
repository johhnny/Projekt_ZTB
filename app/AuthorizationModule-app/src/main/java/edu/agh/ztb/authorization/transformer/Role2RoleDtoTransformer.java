package edu.agh.ztb.authorization.transformer;

import java.util.ArrayList;
import java.util.List;

import edu.agh.ztb.authorization.dto.RoleDto;
import edu.agh.ztb.authorization.model.Permission;
import edu.agh.ztb.authorization.model.Role;
import edu.agh.ztb.authorization.model.RolePermission;

public class Role2RoleDtoTransformer extends AbstractTransformer<Role, RoleDto> {

	private Permission2PermissionDtoTransformer permissionTransformer = new Permission2PermissionDtoTransformer();

	@Override
	public RoleDto transform(Role from) {
		List<Permission> rolePermissions = new ArrayList<Permission>();
		if (from.getRolePermissions() != null) {
			for (RolePermission rolePermission : from.getRolePermissions()) {
				rolePermissions.add(rolePermission.getPermission());
			}
		}
		return RoleDto.builder().name(from.getName()).permissions(permissionTransformer.transformCollection(rolePermissions)).build();
	}

}