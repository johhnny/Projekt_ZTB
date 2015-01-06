package edu.agh.ztb.authorization.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import edu.agh.ztb.authorization.model.Session;
import edu.agh.ztb.authorization.model.User;
import edu.agh.ztb.authorization.test.integration.AbstractIntegrationTest;

public class SessionDaoIntegrationTest extends AbstractIntegrationTest {

	private static final Date START_TIME = DateUtils.addDays(new Date(), -1);
	private static final Date EXPIRATION_TIME_1 = DateUtils.addHours(new Date(), -1);
	private static final Date EXPIRATION_TIME_2 = DateUtils.addHours(new Date(), 2);
	private static final String EXISTING_TOKEN = "123-431-asv-ass";
	private static final String NOT_EXISTING_TOKEN = "not_exisiting";
	private static final String USER_LOGIN = "login";

	@Autowired
	private SessionDao sessionDao;

	@Autowired
	private UserDao userDao;

	@Before
	public void setUp() {
		User user = new User();
		user.setLogin(USER_LOGIN);
		user.setName("name");
		user.setPassword("password");
		user.setSurname("surname");
		userDao.insert(user);

		Session session1 = new Session();
		session1.setExpirationTime(EXPIRATION_TIME_1);
		session1.setStartTime(START_TIME);
		session1.setToken(EXISTING_TOKEN);
		session1.setUser(user);
		session1.setValid(true);
		sessionDao.insert(session1);

		Session session2 = new Session();
		session2.setExpirationTime(EXPIRATION_TIME_2);
		session2.setStartTime(START_TIME);
		session2.setToken(EXISTING_TOKEN + "2");
		session2.setUser(user);
		session2.setValid(true);
		sessionDao.insert(session2);
	}

	@Test
	public void findByToken_shouldFindOneSession() {
		// when
		Session session = sessionDao.findByToken(EXISTING_TOKEN);

		// then
		assertThat(session, is(not(nullValue())));
		assertThat(session.getToken(), is(equalTo(EXISTING_TOKEN)));
		assertThat(session.getStartTime(), is(equalTo(START_TIME)));
		assertThat(session.getExpirationTime(), is(equalTo(EXPIRATION_TIME_1)));
		assertThat(session.getValid(), is(equalTo(true)));
		assertThat(session.getUser(), is(not(nullValue())));
		assertThat(session.getUser().getLogin(), is(equalTo(USER_LOGIN)));
	}

	@Test
	public void findByToken_shouldNotFindSession() {
		// when
		Session session = sessionDao.findByToken(NOT_EXISTING_TOKEN);

		// then
		assertThat(session, is(nullValue()));
	}

	@Test
	public void findExpiredSessions_shouldFindOneSession() {
		// when
		List<Session> sessions = sessionDao.findExpiredSessions();

		// then
		assertThat(sessions, is(not(nullValue())));
		assertThat(sessions, hasSize(1));
		assertThat(sessions.get(0).getToken(), is(equalTo(EXISTING_TOKEN)));
		assertThat(sessions.get(0).getExpirationTime(), is(equalTo(EXPIRATION_TIME_1)));
	}
}