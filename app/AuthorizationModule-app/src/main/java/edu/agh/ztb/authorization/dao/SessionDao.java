package edu.agh.ztb.authorization.dao;

import edu.agh.ztb.authorization.model.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

@Repository
public class SessionDao extends AbstractDao<Session> {

	public SessionDao() {
		super(Session.class);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public Session findByToken(String token) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Session> cq = cb.createQuery(Session.class);
		Root<Session> root = cq.from(Session.class);
		cq.select(root).where(cb.equal(root.get("token"), token));
		Session result = null;
		try {
			result = this.entityManager.createQuery(cq).getSingleResult();
		} catch (NoResultException e) {
		}
		return result;
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public List<Session> findExpiredSessions() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Session> cq = cb.createQuery(Session.class);
		Root<Session> root = cq.from(Session.class);
		Expression<Date> expirationTime = root.get("expirationTime");
		Expression<Boolean> valid = root.get("valid");
		Predicate predicate = cb.and(cb.lessThanOrEqualTo(expirationTime, new Date()), cb.isTrue(valid));
		cq.select(root).where(predicate);
		List<Session> result = this.entityManager.createQuery(cq).getResultList();
		return result;
	}
}