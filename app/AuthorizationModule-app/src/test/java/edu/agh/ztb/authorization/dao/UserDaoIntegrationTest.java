package edu.agh.ztb.authorization.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import edu.agh.ztb.authorization.model.User;
import edu.agh.ztb.authorization.test.integration.AbstractIntegrationTest;

public class UserDaoIntegrationTest extends AbstractIntegrationTest {

	private final static String EXISTING_USER_LOGIN = "existing";
	private final static String NOT_EXISTING_USER_LOGIN = "not_existing";
	private final static String USER_EMAIL = "email";
	private final static String USER_NAME = "name";
	private final static String USER_SURNAME = "surname";
	private final static String USER_PASSWORD = "123";

	@Autowired
	private UserDao userDao;

	@Before
	public void setUp() {
		User user = new User();
		user.setEmail(USER_EMAIL);
		user.setLogin(EXISTING_USER_LOGIN);
		user.setName(USER_NAME);
		user.setPassword(USER_PASSWORD);
		user.setSurname(USER_SURNAME);
		userDao.insert(user);
	}

	@Test
	public void findByLogin_shouldFindUser() {
		// when
		User user = userDao.findByLogin(EXISTING_USER_LOGIN);

		// then
		assertThat(user, is(not(nullValue())));
		assertThat(user.getEmail(), is(equalTo(USER_EMAIL)));
		assertThat(user.getId(), is(not(nullValue())));
		assertThat(user.getLogin(), is(equalTo(EXISTING_USER_LOGIN)));
		assertThat(user.getName(), is(equalTo(USER_NAME)));
		assertThat(user.getSurname(), is(equalTo(USER_SURNAME)));
		assertThat(user.getPassword(), is(equalTo(USER_PASSWORD)));
		assertThat(user.getSessions(), is(nullValue()));
		assertThat(user.getUserRoles(), is(nullValue()));
	}

	@Test
	public void findByLogin_shouldNotFindUser() {
		// when
		User user = userDao.findByLogin(NOT_EXISTING_USER_LOGIN);

		// then
		assertThat(user, is(nullValue()));
	}

	@Test
	public void fetchAll_shouldReturnOneUser() {
		// when
		List<User> users = userDao.fetchAll();

		// then
		assertThat(users, is(not(nullValue())));
		assertThat(users, hasSize(1));
		assertThat(users.get(0).getLogin(), is(equalTo(EXISTING_USER_LOGIN)));
	}

	@Test
	public void update_shouldUpdateEntity() {
		// given
		User user = userDao.findByLogin(EXISTING_USER_LOGIN);
		String newUserName = USER_NAME + "_updated";

		// when
		user.setName(newUserName);
		userDao.update(user);

		// then
		User updatedUser = userDao.findByLogin(EXISTING_USER_LOGIN);
		assertThat(updatedUser, is(not(nullValue())));
		assertThat(updatedUser.getName(), is(not(equalTo(USER_NAME))));
		assertThat(updatedUser.getName(), is(equalTo(newUserName)));
	}

	@Test
	public void delete_shouldDeleteEntity() {
		// given
		User user = userDao.findByLogin(EXISTING_USER_LOGIN);

		// when
		userDao.delete(user);

		// then
		User deletedUser = userDao.findByLogin(EXISTING_USER_LOGIN);
		assertThat(deletedUser, is(nullValue()));
	}
}