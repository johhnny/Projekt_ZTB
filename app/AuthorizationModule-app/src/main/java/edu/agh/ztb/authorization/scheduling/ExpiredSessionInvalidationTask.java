package edu.agh.ztb.authorization.scheduling;

import edu.agh.ztb.authorization.dao.SessionDao;
import edu.agh.ztb.authorization.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("expiredSessionInvalidationTask")
public class ExpiredSessionInvalidationTask {

	@Autowired
	private SessionDao sessionDao;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void invalidateSessions() {
		List<Session> sessions = sessionDao.findExpiredSessions ();
		for (Session session : sessions) {
			session.setValid(false);
			sessionDao.update(session);
		}
	}
}
