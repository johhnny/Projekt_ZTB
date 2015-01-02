package edu.agh.ztb.authorization.dao;

import org.springframework.stereotype.Repository;

import edu.agh.ztb.authorization.model.Role;

@Repository
public class RoleDao extends AbstractDao<Role> {

	public RoleDao() {
		super(Role.class);
	}
}