package edu.agh.ztb.authorization.transformer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import edu.agh.ztb.authorization.dto.PermissionDto;
import edu.agh.ztb.authorization.dto.RoleDto;
import edu.agh.ztb.authorization.model.Permission;
import edu.agh.ztb.authorization.model.Role;
import edu.agh.ztb.authorization.model.RolePermission;

public class Role2RoleDtoTransformerTest {

	private final static String ROLE_NAME = "role_name";
	private final static String ROLE_DESC = "role_desc";
	private final static String PERM1_NAME = "perm1_name";
	private final static String PERM1_DESC = "perm1_desc";
	private final static String PERM2_NAME = "perm2_name";
	private final static String PERM2_DESC = "perm2_desc";

	@Rule
	public ExpectedException exception = ExpectedException.none();

	private Role2RoleDtoTransformer transformer;

	@Before
	public void setUp() {
		transformer = new Role2RoleDtoTransformer();
	}

	@Test
	public void transform_shouldThrowNPE() {
		exception.expect(NullPointerException.class);
		exception.expectMessage("Input must not be null.");

		transformer.transform(null);
	}

	@Test
	public void transformCollection_shouldThrowNPE() {
		exception.expect(NullPointerException.class);
		exception.expectMessage("Input must not be null.");

		transformer.transformCollection(null);
	}

	@Test
	public void transform_shouldReturnCorrectRoleDtoWithoutInnerCollection() {
		// given
		Role role = new Role();
		role.setName(ROLE_NAME);
		role.setDescription(ROLE_DESC);

		// when
		RoleDto roleDto = transformer.transform(role);

		// then
		assertThat(roleDto, is(not(nullValue())));
		assertThat(roleDto.getName(), is(equalTo(ROLE_NAME)));
		assertThat(roleDto.getPermissions(), is(not(nullValue())));
		assertThat(roleDto.getPermissions(), is(emptyCollectionOf(PermissionDto.class)));
	}

	@Test
	public void transform_shouldReturnCorrectRoleDtoWitInnerCollection() {
		// given
		Role role = new Role();
		role.setName(ROLE_NAME);
		role.setDescription(ROLE_DESC);

		Permission permission1 = new Permission();
		permission1.setName(PERM1_NAME);
		permission1.setDescription(PERM1_DESC);
		RolePermission rolePermission1 = new RolePermission();
		rolePermission1.setRole(role);
		rolePermission1.setPermission(permission1);

		Permission permission2 = new Permission();
		permission2.setName(PERM2_NAME);
		permission2.setDescription(PERM2_DESC);
		RolePermission rolePermission2 = new RolePermission();
		rolePermission2.setRole(role);
		rolePermission2.setPermission(permission2);

		role.setRolePermissions(new HashSet<>(Arrays.asList(rolePermission1, rolePermission2)));

		// when
		RoleDto roleDto = transformer.transform(role);

		// then
		assertThat(roleDto, is(not(nullValue())));
		assertThat(roleDto.getName(), is(equalTo(ROLE_NAME)));
		assertThat(roleDto.getPermissions(), is(not(nullValue())));
		assertThat(roleDto.getPermissions(), hasSize(2));
		assertThat(roleDto.getPermissions(),
				containsInAnyOrder(PermissionDto.builder().name(PERM1_NAME).build(), PermissionDto.builder().name(PERM2_NAME).build()));
	}

	@Test
	public void transform_shouldReturnRoleDtoCollection() {
		// given
		Role role1 = new Role();
		role1.setName(ROLE_NAME);
		role1.setDescription(ROLE_DESC);

		Role role2 = new Role();
		role2.setName(ROLE_NAME + "2");
		role2.setDescription(ROLE_DESC + "2");

		// when
		List<RoleDto> roleDtos = transformer.transformCollection(Arrays.asList(role1, role2));

		// then
		assertThat(roleDtos, is(not(nullValue())));
		assertThat(roleDtos, hasSize(2));
		assertThat(
				roleDtos,
				containsInAnyOrder(RoleDto.builder().name(ROLE_NAME).permissions(Collections.<PermissionDto> emptyList()).build(),
						RoleDto.builder().name(ROLE_NAME + "2").permissions(Collections.<PermissionDto> emptyList()).build()));
	}
}