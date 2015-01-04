package edu.agh.ztb.authorization.transformer;

import java.util.ArrayList;
import java.util.List;

import edu.agh.ztb.authorization.dto.RoleDto;
import edu.agh.ztb.authorization.dto.UserDto;
import edu.agh.ztb.authorization.model.Role;
import edu.agh.ztb.authorization.model.User;
import edu.agh.ztb.authorization.model.UserRole;

public class User2UserDtoTransformer extends AbstractTransformer<User, UserDto> {

	private Transformer<Role, RoleDto> role2RoleDtoTransformer = new Role2RoleDtoTransformer();

	@Override
	public UserDto transform(User from) {
		List<Role> userRoles = new ArrayList<Role>();
		if (from.getUserRoles() != null) {
			for (UserRole userRole : from.getUserRoles()) {
				userRoles.add(userRole.getRole());
			}
		}
		return UserDto.builder().email(from.getEmail()).login(from.getLogin()).name(from.getName()).surname(from.getSurname())
				.userRoles(role2RoleDtoTransformer.transformCollection(userRoles)).build();
	}

}