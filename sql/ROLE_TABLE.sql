BEGIN TRANSACTION;

CREATE SEQUENCE ROLE_SEQ START 1;

CREATE TABLE ROLE (
	ID INTEGER PRIMARY KEY DEFAULT NEXTVAL('ROLE_SEQ'),
	NAME VARCHAR(50) UNIQUE NOT NULL,
	DESCRIPTION VARCHAR(250)
);

CREATE INDEX ROLE_NAME_IDX ON ROLE (NAME);

COMMIT;