package edu.agh.ztb.authorization.dao;

import org.springframework.stereotype.Repository;

import edu.agh.ztb.authorization.model.Session;

@Repository
public class SessionDao extends AbstractDao<Session> {

	public SessionDao() {
		super(Session.class);
	}
}