package edu.agh.ztb.authorization.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.agh.ztb.authorization.model.Role;
import edu.agh.ztb.authorization.model.User;
import edu.agh.ztb.authorization.model.UserRole;

@Service
@RestController
public class TestService {

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	@RequestMapping(value = "/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ResponseEntity<?> testService() {
		User user = entityManager.find(User.class, new Long(1l));
		System.out.println(user);

		User user2 = new User();
		user2.setEmail("email");
		user2.setLogin("login");
		user2.setName("name");
		user2.setPassword("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");
		user2.setSurname("surname");
		entityManager.persist(user2);

		UserRole userRole = new UserRole();
		userRole.setUser(user2);
		userRole.setRole(entityManager.find(Role.class, new Long(1l)));
		entityManager.persist(userRole);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
}