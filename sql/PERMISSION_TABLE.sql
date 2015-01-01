BEGIN TRANSACTION;

CREATE SEQUENCE PERMISSION_SEQ START 1;

CREATE TABLE PERMISSION (
	ID INTEGER PRIMARY KEY DEFAULT NEXTVAL('PERMISSION_SEQ'),
	NAME VARCHAR(50) UNIQUE NOT NULL,
	DESCRIPTION VARCHAR(250)
);

CREATE INDEX PERMISSION_NAME_IDX ON PERMISSION (NAME);

COMMIT;