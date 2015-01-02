package edu.agh.ztb.authorization.dao;

import org.springframework.stereotype.Repository;

import edu.agh.ztb.authorization.model.UserRole;

@Repository
public class UserRoleDao extends AbstractDao<UserRole> {

	public UserRoleDao() {
		super(UserRole.class);
	}
}