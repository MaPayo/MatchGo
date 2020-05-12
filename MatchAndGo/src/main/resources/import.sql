-- 
-- El contenido de este fichero se cargará al arrancar la aplicación, suponiendo que uses
-- 		application-default ó application-externaldb en modo 'create'
--

INSERT INTO USER(ID, USERNAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, BIRTH_DATE, GENDER, USER_ROLE, ENABLED,LAST_LOGIN) VALUES (
	1,
	'n',
	'Profesor',
	'Oak',
	'correo',
	'{bcrypt}$2a$10$xLFtBIXGtYvAbRqM95JhcOaG23fHRpDoZIJrsF2cCff9xEHTTdK1u',
	'20/03/1993',
	'Hombre',
	'ADMIN', true,NOW()
);

INSERT INTO USER(ID, USERNAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, BIRTH_DATE, GENDER, USER_ROLE, ENABLED,LAST_LOGIN) VALUES (
	2, 
	'guest',
	'guest',
	'User',
	'correo 3',
	'{bcrypt}$2a$10$xLFtBIXGtYvAbRqM95JhcOaG23fHRpDoZIJrsF2cCff9xEHTTdK1u',
	'fecha_nac 3',
	'Otro', 
	'SYS,GUEST',true,NOW()
);
INSERT INTO USER(ID, USERNAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, BIRTH_DATE, GENDER, USER_ROLE, ENABLED,LAST_LOGIN) VALUES (
	3, 
	'Deleted User',
	'Deleted User',
	'apellidos 4',
	'correo 4',
	'{bcrypt}$2a$10$xLFtBIXGtYvAbRqM95JhcOaG23fHRpDoZIJrsF2cCff9xEHTTdK1u',
	'fecha_nac 4',
	'Mujer', 
	'SYS',true,NOW()
);
INSERT INTO USER(ID, USERNAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, BIRTH_DATE, GENDER, USER_ROLE, ENABLED,LAST_LOGIN) VALUES (
	4, 
	'n2',
	'Ash',
	'Ketchum',
	'correo 2',
	'{bcrypt}$2a$10$xLFtBIXGtYvAbRqM95JhcOaG23fHRpDoZIJrsF2cCff9xEHTTdK1u',
	'fecha_nac 2',
	'Hombre', 
	'USER',true,NOW()
);
INSERT INTO USER(ID, USERNAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, BIRTH_DATE, GENDER, USER_ROLE, ENABLED,LAST_LOGIN) VALUES (
	5, 
	'SS',
	'Sara',
	'Socas',
	'correo 5',
	'{bcrypt}$2a$10$xLFtBIXGtYvAbRqM95JhcOaG23fHRpDoZIJrsF2cCff9xEHTTdK1u',
	'fecha_nac 4',
	'Mujer', 
	'MOD',true,NOW()
);
INSERT INTO USER(ID, USERNAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, BIRTH_DATE, GENDER, USER_ROLE, ENABLED, LAST_LOGIN) VALUES (
	6, 
	'Bn',
	'Baron',
	'Morningstar',
	'correo 6',
	'{bcrypt}$2a$10$xLFtBIXGtYvAbRqM95JhcOaG23fHRpDoZIJrsF2cCff9xEHTTdK1u',
	'fecha_nac 4',
	'Mujer', 
	'MOD',true,NOW()
);
INSERT INTO EVENT(ID, NAME, DESCRIPTION, LOCATION, DATE, PUBLICATION_DATE, IS_APPROPRIATE,CREATOR_ID) VALUES (
	1,
	'Nombre Evento 1',
	'Descripcion Evnto 1',
	'El retiro',
	NOW(),
	NOW(),
	true,
	4
);

INSERT INTO EVENT(ID, NAME, DESCRIPTION, LOCATION, DATE, PUBLICATION_DATE,CREATOR_ID) VALUES (
	2,
	'Nombre Evento 2',
	'Descripcion Evnto 2',
	'El retiro',
	NOW(),
	NOW(),
	4
);

INSERT INTO EVENT(ID, NAME, DESCRIPTION, LOCATION, DATE, PUBLICATION_DATE,CREATOR_ID) VALUES (
	3,
	'Nombre Evento 3',
	'Descripcion Evnto 3',
	'El retiro',
	NOW + 2 DAY,
	NOW(),
	5
);

INSERT INTO EVENT (ID, NAME, DESCRIPTION, LOCATION, DATE, PUBLICATION_DATE,IS_APPROPRIATE,CREATOR_ID) VALUES (
	4,
	'Nombre Evento 4',
	'Descripcion Evnto 4',
	'El retiro',
	NOW(),
	NOW(),
	true,
	6
);



INSERT INTO EVALUATION(ID, SCORE, REVIEW,EVALUATOR_ID,EVALUATED_ID) VALUES(1, 2, 'Es muy posesivo con el balón. No juega en equipo',4,5);
INSERT INTO EVALUATION(ID, SCORE, REVIEW,EVALUATOR_ID,EVALUATED_ID) VALUES(2, 5, 'Me ayudó a subir de bronce a platino en una semana',4,6);
INSERT INTO EVALUATION(ID, SCORE, REVIEW,EVALUATOR_ID,EVALUATED_ID) VALUES(3, 3, 'Cocina como todo el mundo, nada fuera de serie.',6,5);
INSERT INTO EVALUATION(ID, SCORE, REVIEW,EVALUATOR_ID,EVALUATED_ID) VALUES(4, 1, 'Su perro me ha mordido.',5,6);
INSERT INTO TAGS (ID, IS_CATEGORY, TAG) VALUES(1, true, 'sport');
INSERT INTO TAGS (ID, IS_CATEGORY, TAG) VALUES(2, true, 'game');
INSERT INTO TAGS (ID, IS_CATEGORY, TAG) VALUES(3, true, 'food');
INSERT INTO TAGS (ID, IS_CATEGORY, TAG) VALUES(4, true, 'pet');
INSERT INTO EVENT_TAGS(EVENTS_ID, TAGS_ID) VALUES(1, 1);
INSERT INTO EVENT_TAGS(EVENTS_ID, TAGS_ID) VALUES(2, 1);
INSERT INTO EVENT_TAGS(EVENTS_ID, TAGS_ID) VALUES(3, 1);
INSERT INTO EVENT_TAGS(EVENTS_ID, TAGS_ID) VALUES(3, 2);

INSERT INTO EVENT_TAGS VALUES(4, 1);

INSERT INTO EVENT_PARTICIPANTS VALUES(1,4);
INSERT INTO EVENT_PARTICIPANTS VALUES(1,5);
INSERT INTO EVENT_PARTICIPANTS VALUES(3,4);
INSERT INTO EVENT_PARTICIPANTS VALUES(3,5);
INSERT INTO EVENT_PARTICIPANTS VALUES(3,6);
INSERT INTO EVENT_PARTICIPANTS VALUES(4,5);
INSERT INTO EVENT_PARTICIPANTS VALUES(1,6);
INSERT INTO USER_TAGS VALUES(4, 1);
INSERT INTO USER_TAGS VALUES(4, 2);
INSERT INTO USER_TAGS VALUES(5, 1);
INSERT INTO USER_TAGS VALUES(5, 3);
INSERT INTO USER_TAGS VALUES(6, 2);

INSERT INTO MESSAGE (ID, text_message, sender_id, receiver_id, send_date, read_message) VALUES (
	1,
	'Hola que tal',
	1,
	2,
	NOW(),
	1
);

INSERT INTO MESSAGE (ID, text_message, sender_id, receiver_id, send_date, read_message) VALUES (
	2,
	'Bien y tu',
	2,
	1,
	NOW(),
	1
);

INSERT INTO MESSAGE (ID, text_message, sender_id, receiver_id, send_date, read_message) VALUES (
	3,
	'Yo tambien bien',
	1,
	2,
	NOW(),
	1
);

INSERT INTO MESSAGE (ID, text_message, sender_id, receiver_id, send_date, read_message) VALUES (
	4,
	'Eyyyyyy',
	1,
	3,
	NOW(),
	1
);

INSERT INTO MESSAGE (ID, text_message, sender_id, receiver_id, send_date, read_message) VALUES (
	5,
	'Quien eres',
	3,
	1,
	NOW(),
	1
);


INSERT INTO MESSAGE (ID, text_message, sender_id, receiver_id, send_date, read_message, ID_EVENT_ID) VALUES (6,'Hola que tal',4,null,NOW(),0,1);
INSERT INTO MESSAGE (ID, text_message, sender_id, receiver_id, send_date, read_message, ID_EVENT_ID) VALUES (7,'que tal',5,null,NOW(),0,1);
