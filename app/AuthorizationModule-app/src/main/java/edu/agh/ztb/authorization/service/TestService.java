package edu.agh.ztb.authorization.service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.agh.ztb.authorization.model.User;

@Service
public class TestService {

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	@PostConstruct
	@Transactional
	public void testService() {
		User user = entityManager.find(User.class, new Long(1l));
		System.out.println(user);
	}
}