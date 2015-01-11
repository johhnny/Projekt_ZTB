BEGIN TRANSACTION;
CREATE EXTENSION pgcrypto;
insert into users(id, login, password,name, surname, email) values (1, 'gis', encode(digest('gis', 'sha256'),'hex'), 'gis', 'gis', 'gis@agh');
insert into users(id, login, password,name, surname, email) values (2, 'obj', encode(digest('obj', 'sha256'),'hex'), 'obj', 'objects', 'obj@agh');
insert into users(id, login, password,name, surname, email) values (3, 'sen', encode(digest('sen', 'sha256'),'hex'), 'sen', 'sensors', 'sen@agh');
insert into users(id, login, password,name, surname, email) values (4, 'req', encode(digest('req', 'sha256'),'hex'), 'req', 'requirements', 'req@agh');
insert into users(id, login, password,name, surname, email) values (5, 'rul', encode(digest('rul', 'sha256'),'hex'), 'rul', 'rules', 'rul@agh');
insert into users(id, login, password,name, surname, email) values (6, 'profiles', encode(digest('profiles', 'sha256'),'hex'), 'profiles', 'profiles', 'profiles@agh');
insert into users(id, login, password,name, surname, email) values (7, 'all', encode(digest('all', 'sha256'),'hex'), 'All', 'Permissions', 'all@agh');
insert into users(id, login, password,name, surname, email) values (8, 'noperm', encode(digest('noperm', 'sha256'),'hex'), 'No', 'Permission', 'noperm@agh');
ALTER SEQUENCE USERS_SEQ INCREMENT BY 8;

insert into permission(id, name, description) values(1, 'GIS_CRUD', 'Allows to create, read, update and delete GIS data (streets, buildings, trees etc.)');
insert into permission(id, name, description) values(2, 'OBJECTS_CRUD', 'Allows to create, read, update and delete objects (fixtures, drivers, cabinets, segment controllers etc.)');
insert into permission(id, name, description) values(3, 'SENSORS_CRUD', 'Allows to create, read, update and delete sensors');
insert into permission(id, name, description) values(4, 'REQUIREMENTS_CRUD', 'Allows to create, read, update and delete requirements');
insert into permission(id, name, description) values(5, 'RULES_CRUD', 'Allows to create, read, update and delete rules');
insert into permission(id, name, description) values(6, 'PROFILES_CRUD', 'Allows to create, read, update and delete profiles and configuration');
ALTER SEQUENCE PERMISSION_SEQ INCREMENT BY 6;

insert into role(id, name, description) values(1, 'GIS_CRUD', 'GIS role');
insert into role(id, name, description) values(2, 'OBJECTS_CRUD', 'Objects role');
insert into role(id, name, description) values(3, 'SENSORS_CRUD', 'Sensors role');
insert into role(id, name, description) values(4, 'REQUIREMENTS_CRUD', 'Requirements role');
insert into role(id, name, description) values(5, 'RULES_CRUD', 'Rules role');
insert into role(id, name, description) values(6, 'PROFILES_CRUD', 'Profiles role');
insert into role(id, name, description) values(7, 'ALL_MODULES_CRUD', 'Includes all modules permissions');
ALTER SEQUENCE ROLE_SEQ INCREMENT BY 7;

insert into role_permission(role_id, permission_id) values (1,1);
insert into role_permission(role_id, permission_id) values (2,2);
insert into role_permission(role_id, permission_id) values (3,3);
insert into role_permission(role_id, permission_id) values (4,4);
insert into role_permission(role_id, permission_id) values (5,5);
insert into role_permission(role_id, permission_id) values (6,6);

insert into role_permission(role_id, permission_id) values (7,1);
insert into role_permission(role_id, permission_id) values (7,2);
insert into role_permission(role_id, permission_id) values (7,3);
insert into role_permission(role_id, permission_id) values (7,4);
insert into role_permission(role_id, permission_id) values (7,5);
insert into role_permission(role_id, permission_id) values (7,6);

insert into user_role(user_id, role_id) values (1,1);
insert into user_role(user_id, role_id) values (2,2);
insert into user_role(user_id, role_id) values (3,3);
insert into user_role(user_id, role_id) values (4,4);
insert into user_role(user_id, role_id) values (5,5);
insert into user_role(user_id, role_id) values (6,6);
insert into user_role(user_id, role_id) values (7,7);

COMMIT;