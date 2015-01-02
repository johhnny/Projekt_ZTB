package edu.agh.ztb.authorization.dao;

import org.springframework.stereotype.Repository;

import edu.agh.ztb.authorization.model.RolePermission;

@Repository
public class RolePermissionDao extends AbstractDao<RolePermission> {

	public RolePermissionDao() {
		super(RolePermission.class);
	}
}