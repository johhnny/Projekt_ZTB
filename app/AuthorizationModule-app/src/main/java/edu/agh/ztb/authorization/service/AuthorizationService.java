package edu.agh.ztb.authorization.service;

import edu.agh.ztb.authorization.dao.SessionDao;
import edu.agh.ztb.authorization.dao.UserDao;
import edu.agh.ztb.authorization.dto.ErrorWrapper;
import edu.agh.ztb.authorization.dto.UserDto;
import edu.agh.ztb.authorization.model.RolePermission;
import edu.agh.ztb.authorization.model.Session;
import edu.agh.ztb.authorization.model.User;
import edu.agh.ztb.authorization.model.UserRole;
import edu.agh.ztb.authorization.transformer.User2UserDtoTransformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

@RestController
public class AuthorizationService {

	private static final int TOKEN_VALIDITY_DAYS = 30;

	@Autowired
	private UserDao userDao;

	@Autowired
	private SessionDao sessionDao;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ResponseEntity<?> authenticate(@RequestParam("login") String login, @RequestParam("password") String password) {
		User user = userDao.findByLogin(login);
		String passwordHash = createHash(password);
		if (user == null) {
			return new ResponseEntity<>(ErrorWrapper.builder().errorCode(-1).errorMessage("User not found for given login").build(), HttpStatus.BAD_REQUEST);
		} else if (!StringUtils.equalsIgnoreCase(user.getPassword(), passwordHash)) {
			return new ResponseEntity<>(ErrorWrapper.builder().errorCode(-1).errorMessage("User/Password combination is incorrect").build(), HttpStatus.BAD_REQUEST);
		}
		Session session = new Session();
		session.setStartTime(new Date());
		session.setToken(UUID.randomUUID().toString());
		session.setUser(user);
		session.setExpirationTime(DateUtils.addDays(new Date(), TOKEN_VALIDITY_DAYS));
		sessionDao.insert(session);
		UserDto userDto = new User2UserDtoTransformer().transform(user);
		userDto.setCurrentToken(session.getToken());
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}

	@RequestMapping(value = "/invalidateToken", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ResponseEntity<?> invalidateToken(@RequestParam("token") String token) {
		Session session = sessionDao.findByToken(token);
		if (session == null) {
			return new ResponseEntity<>(ErrorWrapper.builder().errorCode(-1).errorMessage("Session not found for given token").build(), HttpStatus.BAD_REQUEST);
		}
		session.setValid(false);
		sessionDao.update(session);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/checkPermissionForToken", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ResponseEntity<?> checkPermissionForToken(@RequestParam("token") String token, @RequestParam("permission") String permission) {
		Session session = sessionDao.findByToken(token);
		if (session == null || isTokenExpired(session)) {
			return new ResponseEntity<>(ErrorWrapper.builder().errorCode(-1).errorMessage("No active session found for given token").build(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(hasPermission(session.getUser(), permission), HttpStatus.OK);
	}

	@RequestMapping(value = "/getUserForToken", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ResponseEntity<?> getUserForToken(@RequestParam("token") String token) {
		Session session = sessionDao.findByToken(token);
		if (session == null || isTokenExpired(session)) {
			return new ResponseEntity<>(ErrorWrapper.builder().errorCode(-1).errorMessage("No active session found for given token").build(), HttpStatus.BAD_REQUEST);
		}
		UserDto userDto = new User2UserDtoTransformer().transform(session.getUser());
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}

	private boolean hasPermission(User user, String permission) {
		for (UserRole userRole : user.getUserRoles()) {
			for (RolePermission userPermission : userRole.getRole().getRolePermissions()) {
				if (userPermission.getPermission().getName().equals(permission)) {
					return true;
				}
			}
		}
		return false;
	}

	private String createHash(String text) {
		MessageDigest md;
		String result = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(text.getBytes("UTF-8"));
			byte[] digest = md.digest();
			result = DatatypeConverter.printHexBinary(digest);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	private boolean isTokenExpired(Session session) {
		return (new Date()).compareTo(session.getExpirationTime()) >= 0 || !session.getValid();
	}

}
