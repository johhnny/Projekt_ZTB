package edu.agh.ztb.authorization.dao;

import edu.agh.ztb.authorization.model.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class SessionDao extends AbstractDao<Session> {

	public SessionDao() {
		super(Session.class);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public Session findByToken(String login) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Session> cq = cb.createQuery(Session.class);
		Root<Session> root = cq.from(Session.class);
		cq.select(root).where(cb.equal(root.get("token"), login));
		Session result = null;
		try {
			result = this.entityManager.createQuery(cq).getSingleResult();
		} catch (NoResultException e) {
		}
		return result;
	}
}