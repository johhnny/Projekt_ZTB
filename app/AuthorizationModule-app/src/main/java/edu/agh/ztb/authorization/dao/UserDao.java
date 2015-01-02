package edu.agh.ztb.authorization.dao;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import edu.agh.ztb.authorization.model.User;

@Repository
public class UserDao extends AbstractDao<User> {

	public UserDao() {
		super(User.class);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public User findByLogin(String login) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> root = cq.from(User.class);
		cq.select(root).where(cb.equal(root.get("login"), login));
		User result = null;
		try {
			result = this.entityManager.createQuery(cq).getSingleResult();
		} catch (NoResultException e) {}
		return result;
	}
}