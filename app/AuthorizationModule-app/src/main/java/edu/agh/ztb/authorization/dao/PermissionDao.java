package edu.agh.ztb.authorization.dao;

import org.springframework.stereotype.Repository;

import edu.agh.ztb.authorization.model.Permission;

@Repository
public class PermissionDao extends AbstractDao<Permission> {

	public PermissionDao() {
		super(Permission.class);
	}
}