package edu.agh.ztb.authorization.service;

import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import edu.agh.ztb.authorization.dao.UserDao;
import edu.agh.ztb.authorization.dto.ErrorWrapper;
import edu.agh.ztb.authorization.dto.RoleDto;
import edu.agh.ztb.authorization.dto.UserDto;
import edu.agh.ztb.authorization.model.Role;
import edu.agh.ztb.authorization.model.User;
import edu.agh.ztb.authorization.model.UserRole;
import edu.agh.ztb.authorization.transformer.User2UserDtoTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Service
@RestController
public class TestService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private UserDao userDao;

	@RequestMapping(value = "/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ResponseEntity<User> testService() {
		User user = entityManager.find(User.class, new Long(1l));
		System.out.println(user);

		User user2 = new User();
		user2.setEmail("email");
		user2.setLogin(String.valueOf(new Random().nextInt()));
		user2.setName("name");
		user2.setPassword("a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3");
		user2.setSurname("surname");
		entityManager.persist(user2);

		UserRole userRole = new UserRole();
		userRole.setUser(user2);
		userRole.setRole(entityManager.find(Role.class, new Long(1l)));
		entityManager.persist(userRole);

		return new ResponseEntity<User>(user2, HttpStatus.OK);
	}

	@RequestMapping(value = "/principals/{login}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ResponseEntity<?> fetchUserPrincipals(@PathVariable String login) {
		User user = userDao.findByLogin(login);
		if (user == null) {
			return new ResponseEntity<ErrorWrapper>(ErrorWrapper.builder().errorCode(-1).errorMessage("User not found for given login").build(), HttpStatus.BAD_REQUEST);
		}
		UserDto transformed = new User2UserDtoTransformer().transform(user);
		return new ResponseEntity<List<RoleDto>>(transformed.getUserRoles(), HttpStatus.OK);
	}
}