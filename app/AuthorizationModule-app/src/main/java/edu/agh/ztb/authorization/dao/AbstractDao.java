package edu.agh.ztb.authorization.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractDao<T> {

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager entityManager;

	protected final Class<T> entityClass;

	public AbstractDao(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void insert(T entity) {
		this.entityManager.persist(entity);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void update(T entity) {
		this.entityManager.merge(entity);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void delete(T entity) {
		this.entityManager.remove(entity);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public List<T> fetchAll() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(entityClass);
		cq.select(cq.from(entityClass));
		return this.entityManager.createQuery(cq).getResultList();
	}
}