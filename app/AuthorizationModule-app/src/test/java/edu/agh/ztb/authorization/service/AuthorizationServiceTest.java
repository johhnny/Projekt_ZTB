package edu.agh.ztb.authorization.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import edu.agh.ztb.authorization.dao.SessionDao;
import edu.agh.ztb.authorization.dao.UserDao;
import edu.agh.ztb.authorization.dto.ErrorWrapper;
import edu.agh.ztb.authorization.dto.UserDto;
import edu.agh.ztb.authorization.model.Permission;
import edu.agh.ztb.authorization.model.Role;
import edu.agh.ztb.authorization.model.RolePermission;
import edu.agh.ztb.authorization.model.Session;
import edu.agh.ztb.authorization.model.User;
import edu.agh.ztb.authorization.model.UserRole;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthorizationServiceTest {

	private static final String VALID_TOKEN = "qwe123123-sdfdsf-23423432-rewr";
	private static final String INVALID_TOKEN = "qwe123123-sdfdsf-23423432-reiv";

	private static final String LOGIN = "qwe";
	private static final String CORRECT_PASSWORD = "123";
	private static final String PASSWORD_HASH = "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3";
	private static final String INCORRECT_PASSWORD = "123q";
	public static final String CORRECT_PERMISSION_NAME = "CRUD_CABINET";
	public static final String INCORRECT_PERMISSION_NAME = "CRUD_CABINET2";

	private SessionDao sessionDao;

	private UserDao userDao;

	private AuthorizationService authorizationService;

	@Before
	public void setUp() {
		sessionDao = mock(SessionDao.class);
		userDao = mock(UserDao.class);
		authorizationService = new AuthorizationService();
		authorizationService.setSessionDao(sessionDao);
		authorizationService.setUserDao(userDao);
	}

	@Test
	public void authenticate_shouldReturnValidAuthenticationToken() {
		//given
		User user = new User();
		user.setLogin(LOGIN);
		user.setPassword(PASSWORD_HASH);
		when(userDao.findByLogin(LOGIN)).thenReturn(user);

		//when
		ResponseEntity<?> response = authorizationService.authenticate(LOGIN, CORRECT_PASSWORD);

		//then
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Object body = response.getBody();
		Assert.assertEquals(UserDto.class, body.getClass());
		UserDto userDto = (UserDto) body;
		assertThat(userDto.getLogin(), equalTo(LOGIN));
		verify(sessionDao, times(1)).insert(any(Session.class));
	}

	@Test
	public void authenticate_shouldReturnIncorrectUserPasswordCombinationError() {
		//given
		User user = new User();
		user.setLogin(LOGIN);
		user.setPassword(PASSWORD_HASH);
		when(userDao.findByLogin(LOGIN)).thenReturn(user);

		//when
		ResponseEntity<?> response = authorizationService.authenticate(LOGIN, INCORRECT_PASSWORD);

		//then
		assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
		Object body = response.getBody();
		Assert.assertEquals(ErrorWrapper.class, body.getClass());
		ErrorWrapper error = (ErrorWrapper) body;
		assertThat(error.getErrorMessage(), containsString("User/Password combination is incorrect"));
		verify(sessionDao, never()).insert(any(Session.class));
	}

	@Test
	public void invalidateToken_shouldInvalidateToken() {
		//given
		Session session = new Session();
		session.setToken(VALID_TOKEN);
		session.setValid(true);
		//rest
		when(sessionDao.findByToken(VALID_TOKEN)).thenReturn(session);

		//when
		ResponseEntity<?> response = authorizationService.invalidateToken(VALID_TOKEN);

		//then
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(session.getValid(), equalTo(false));
		verify(sessionDao, times(1)).update(session);
	}

	@Test
	public void invalidateToken_shouldReturnTokenInvalidationError() {
		//given
		Session session = new Session();
		session.setToken(VALID_TOKEN);
		session.setValid(false);
		when(sessionDao.findByToken(VALID_TOKEN)).thenReturn(session);

		//when
		ResponseEntity<?> response = authorizationService.invalidateToken(INVALID_TOKEN);

		//then
		assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
		Object body = response.getBody();
		assertThat(body, instanceOf(ErrorWrapper.class));
		ErrorWrapper error = (ErrorWrapper) body;
		assertThat(error.getErrorMessage(), containsString("Session not found for given token"));
		verify(sessionDao, never()).update(session);
	}

	@Test
	public void checkPermissionForToken_shouldReturnTrueForPermission() {
		//given
		Session session = new Session();
		session.setToken(VALID_TOKEN);
		session.setExpirationTime(DateUtils.addDays(new Date(), 1));
		session.setValid(true);
		User user = new User();
		UserRole ur = new UserRole();
		Role role = new Role();
		RolePermission rp = new RolePermission();
		Permission p = new Permission();
		p.setName(CORRECT_PERMISSION_NAME);
		role.setName("ADMINISTRATOR");
		rp.setPermission(p);
		rp.setRole(role);
		ur.setUser(user);
		ur.setRole(role);
		role.setRolePermissions(new HashSet<>(Arrays.asList(rp)));
		user.setUserRoles(new HashSet<>(Arrays.asList(ur)));
		session.setUser(user);
		when(sessionDao.findByToken(VALID_TOKEN)).thenReturn(session);

		//when
		ResponseEntity<?> response = authorizationService.checkPermissionForToken(VALID_TOKEN, CORRECT_PERMISSION_NAME);

		//then
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		Object body = response.getBody();
		assertThat(body, instanceOf(Boolean.class));
		Boolean result = (Boolean) body;
		assertThat(result, equalTo(true));
	}

	@Test
	public void checkPermissionForToken_shouldReturnFalseForPermission() {
		//given
		Session session = new Session();
		session.setToken(VALID_TOKEN);
		session.setValid(true);
		session.setExpirationTime(DateUtils.addDays(new Date(), 1));
		User user = new User();
		UserRole ur = new UserRole();
		Role role = new Role();
		RolePermission rp = new RolePermission();
		Permission p = new Permission();
		p.setName(CORRECT_PERMISSION_NAME);
		role.setName("ADMINISTRATOR");
		rp.setPermission(p);
		rp.setRole(role);
		ur.setUser(user);
		ur.setRole(role);
		role.setRolePermissions(new HashSet<>(Arrays.asList(rp)));
		user.setUserRoles(new HashSet<>(Arrays.asList(ur)));
		session.setUser(user);
		when(sessionDao.findByToken(VALID_TOKEN)).thenReturn(session);

		//when
		ResponseEntity<?> response = authorizationService.checkPermissionForToken(VALID_TOKEN, INCORRECT_PERMISSION_NAME);

		//then
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		Object body = response.getBody();
		assertThat(body, instanceOf(Boolean.class));
		boolean result = (boolean) body;
		assertThat(result, equalTo(false));
	}

	@Test
	public void checkPermissionForToken_shouldReturnNoActiveSessionError() {
		//given
		Session session = new Session();
		session.setToken(VALID_TOKEN);
		session.setValid(false);
		session.setExpirationTime(DateUtils.addDays(new Date(), -100));
		User user = new User();
		UserRole ur = new UserRole();
		Role role = new Role();
		RolePermission rp = new RolePermission();
		Permission p = new Permission();
		p.setName(CORRECT_PERMISSION_NAME);
		role.setName("ADMINISTRATOR");
		rp.setPermission(p);
		rp.setRole(role);
		ur.setUser(user);
		ur.setRole(role);
		role.setRolePermissions(new HashSet<>(Arrays.asList(rp)));
		user.setUserRoles(new HashSet<>(Arrays.asList(ur)));
		session.setUser(user);
		when(sessionDao.findByToken(VALID_TOKEN)).thenReturn(session);

		//when
		ResponseEntity<?> response = authorizationService.checkPermissionForToken(VALID_TOKEN, INCORRECT_PERMISSION_NAME);

		//then
		assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
		Object body = response.getBody();
		assertThat(body, instanceOf(ErrorWrapper.class));
		ErrorWrapper error = (ErrorWrapper) body;
		assertThat(error.getErrorMessage(), containsString("No active session found for given token"));
	}

	@Test
	public void getUserForToken_shouldReturnUserForToken() {
		//given
		Session session = new Session();
		session.setToken(VALID_TOKEN);
		session.setValid(true);
		session.setExpirationTime(DateUtils.addDays(new Date(), 1));
		User user = new User();
		user.setLogin(LOGIN);
		session.setUser(user);
		when(sessionDao.findByToken(VALID_TOKEN)).thenReturn(session);

		//when
		ResponseEntity<?> response = authorizationService.getUserForToken(VALID_TOKEN);

		//then
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		Object body = response.getBody();
		assertThat(body, instanceOf(UserDto.class));
		UserDto result = (UserDto) body;
		assertThat(result.getLogin(), equalTo(LOGIN));
	}

	@Test
	public void getUserForToken_shouldReturnNoActiveSessionError() {
		//given
		Session session = new Session();
		session.setToken(INVALID_TOKEN);
		session.setValid(false);
		session.setExpirationTime(DateUtils.addDays(new Date(), -100));
		when(sessionDao.findByToken(INVALID_TOKEN)).thenReturn(session);

		//when
		ResponseEntity<?> response = authorizationService.getUserForToken(INVALID_TOKEN);

		//then
		assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
		Object body = response.getBody();
		assertThat(body, instanceOf(ErrorWrapper.class));
		ErrorWrapper result = (ErrorWrapper) body;
		assertThat(result.getErrorMessage(), containsString("No active session found for given token"));
	}

}
