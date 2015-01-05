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
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import edu.agh.ztb.authorization.dto.PermissionDto;
import edu.agh.ztb.authorization.dto.RoleDto;
import edu.agh.ztb.authorization.dto.UserDto;
import edu.agh.ztb.authorization.model.Role;
import edu.agh.ztb.authorization.model.User;
import edu.agh.ztb.authorization.model.UserRole;

public class User2UserDtoTransformerTest {

	private final static String USER_LOGIN = "login";
	private final static String USER_NAME = "name";
	private final static String USER_SURNAME = "surname";
	private final static String USER_EMAIL = "123@321.pl";
	private final static String USER_PASSWORD = "12345678";
	private final static String ROLE1_NAME = "role1_name";
	private final static String ROLE1_DESC = "role1_desc";
	private final static String ROLE2_NAME = "role2_name";
	private final static String ROLE2_DESC = "role2_desc";

	@Rule
	public ExpectedException exception = ExpectedException.none();

	private User2UserDtoTransformer transformer;

	@Before
	public void setUp() {
		transformer = new User2UserDtoTransformer();
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
	public void transform_shouldReturnCorrectUserDtoWithoutInnerCollection() {
		// given
		User user = new User();
		user.setLogin(USER_LOGIN);
		user.setName(USER_NAME);
		user.setSurname(USER_SURNAME);
		user.setEmail(USER_EMAIL);
		user.setPassword(USER_PASSWORD);

		// when
		UserDto userDto = transformer.transform(user);

		// then
		assertThat(userDto, is(not(nullValue())));
		assertThat(userDto.getLogin(), is(equalTo(USER_LOGIN)));
		assertThat(userDto.getName(), is(equalTo(USER_NAME)));
		assertThat(userDto.getSurname(), is(equalTo(USER_SURNAME)));
		assertThat(userDto.getEmail(), is(equalTo(USER_EMAIL)));
		assertThat(userDto.getCurrentToken(), is(nullValue()));
		assertThat(userDto.getUserRoles(), is(not(nullValue())));
		assertThat(userDto.getUserRoles(), is(emptyCollectionOf(RoleDto.class)));
	}

	@Test
	public void transform_shouldReturnCorrectUserDtoWithInnerCollection() {
		// given
		User user = new User();
		user.setLogin(USER_LOGIN);
		user.setName(USER_NAME);
		user.setSurname(USER_SURNAME);
		user.setEmail(USER_EMAIL);
		user.setPassword(USER_PASSWORD);
		user.setUserRoles(prepareUserRolesCollection(user));

		// when
		UserDto userDto = transformer.transform(user);

		// then
		assertThat(userDto, is(not(nullValue())));
		assertThat(userDto.getLogin(), is(equalTo(USER_LOGIN)));
		assertThat(userDto.getName(), is(equalTo(USER_NAME)));
		assertThat(userDto.getSurname(), is(equalTo(USER_SURNAME)));
		assertThat(userDto.getEmail(), is(equalTo(USER_EMAIL)));
		assertThat(userDto.getCurrentToken(), is(nullValue()));
		assertThat(userDto.getUserRoles(), is(not(nullValue())));
		assertThat(userDto.getUserRoles(), hasSize(2));
		assertThat(
				userDto.getUserRoles(),
				containsInAnyOrder(RoleDto.builder().name(ROLE1_NAME).permissions(Collections.<PermissionDto> emptyList()).build(),
						RoleDto.builder().name(ROLE2_NAME).permissions(Collections.<PermissionDto> emptyList()).build()));
	}

	@Test
	public void transformCollection_shouldReturnCorrectUserDtoList() {
		// given
		User user1 = new User();
		user1.setLogin(USER_LOGIN);
		user1.setName(USER_NAME);
		user1.setSurname(USER_SURNAME);
		user1.setEmail(USER_EMAIL);
		user1.setPassword(USER_PASSWORD);

		User user2 = new User();
		user2.setLogin(USER_LOGIN + "2");
		user2.setName(USER_NAME + "2");
		user2.setSurname(USER_SURNAME + "2");
		user2.setEmail(USER_EMAIL + "2");
		user2.setPassword(USER_PASSWORD + "2");

		// when
		List<UserDto> userDtos = transformer.transformCollection(Arrays.asList(user1, user2));

		// then
		assertThat(userDtos, is(not(nullValue())));
		assertThat(userDtos, hasSize(2));
		assertThat(
				userDtos,
				containsInAnyOrder(
						UserDto.builder().login(USER_LOGIN).name(USER_NAME).surname(USER_SURNAME).email(USER_EMAIL)
								.userRoles(Collections.<RoleDto> emptyList()).build(),
						UserDto.builder().login(USER_LOGIN + "2").name(USER_NAME + "2").surname(USER_SURNAME + "2").email(USER_EMAIL + "2")
								.userRoles(Collections.<RoleDto> emptyList()).build()));
	}

	private Set<UserRole> prepareUserRolesCollection(User user) {
		Set<UserRole> userRoles = new HashSet<>();

		Role role1 = new Role();
		role1.setName(ROLE1_NAME);
		role1.setDescription(ROLE1_DESC);

		Role role2 = new Role();
		role2.setName(ROLE2_NAME);
		role2.setDescription(ROLE2_DESC);

		UserRole userRole1 = new UserRole();
		userRole1.setRole(role1);
		userRole1.setUser(user);

		UserRole userRole2 = new UserRole();
		userRole2.setRole(role2);
		userRole2.setUser(user);
		userRoles.add(userRole1);
		userRoles.add(userRole2);
		return userRoles;
	}
}