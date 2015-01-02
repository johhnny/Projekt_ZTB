package edu.agh.ztb.authorization.transformer;

import edu.agh.ztb.authorization.dto.PermissionDto;
import edu.agh.ztb.authorization.model.Permission;

public class Permission2PermissionDtoTransformer extends AbstractTransformer<Permission, PermissionDto> {

	@Override
	public PermissionDto transform(Permission from) {
		return PermissionDto.builder().name(from.getName()).build();
	}
}