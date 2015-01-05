package edu.agh.ztb.authorization.scheduling;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.agh.ztb.authorization.dao.SessionDao;
import edu.agh.ztb.authorization.model.Session;

public class ExpiredSessionInvalidationTaskTest {

	private final static String TOKEN = "1234-abcd-12-ab";

	private ExpiredSessionInvalidationTask task;
	private SessionDao sessionDao;

	@Before
	public void setUp() {
		sessionDao = mock(SessionDao.class);
		task = new ExpiredSessionInvalidationTask();
		task.sessionDao = sessionDao;
	}

	@Test
	public void invalidateSessions_shouldInvalidateAllSessions() {
		// given
		Session session1 = new Session();
		session1.setToken(TOKEN);
		session1.setValid(true);

		Session session2 = new Session();
		session2.setToken(TOKEN);
		session2.setValid(true);
		when(sessionDao.findExpiredSessions()).thenReturn(Arrays.asList(session1, session2));

		// when
		task.invalidateSessions();

		// then
		assertThat(session1.getValid(), is(equalTo(false)));
		assertThat(session1.getToken(), is(equalTo(TOKEN)));
		assertThat(session2.getValid(), is(equalTo(false)));
		assertThat(session2.getToken(), is(equalTo(TOKEN)));
		verify(sessionDao, times(1)).findExpiredSessions();
		verify(sessionDao, times(2)).update((Session) anyObject());
	}

	@Test
	public void invalidateSessions_shouldDoNothing() {
		// given
		when(sessionDao.findExpiredSessions()).thenReturn(Collections.<Session> emptyList());

		// when
		task.invalidateSessions();

		// then
		verify(sessionDao, times(1)).findExpiredSessions();
		verify(sessionDao, never()).update((Session) anyObject());
	}

	@Test
	public void invalidateSessions_shouldDoNothingWhenNullListReturnedFromDao() {
		// given
		when(sessionDao.findExpiredSessions()).thenReturn(null);

		// when
		task.invalidateSessions();

		// then
		verify(sessionDao, times(1)).findExpiredSessions();
		verify(sessionDao, never()).update((Session) anyObject());
	}
}