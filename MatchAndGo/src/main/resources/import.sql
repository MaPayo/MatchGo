-- 
-- El contenido de este fichero se cargará al arrancar la aplicación, suponiendo que uses
-- 		application-default ó application-externaldb en modo 'create'
--

INSERT INTO USER(ID, USERNAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, BIRTH_DATE, GENDER, USER_ROLE, ENABLED) VALUES (
	1,
	'n',
	'nombre',
	'apellidos',
	'correo',
	'{bcrypt}$2a$10$xLFtBIXGtYvAbRqM95JhcOaG23fHRpDoZIJrsF2cCff9xEHTTdK1u',
	'fecha_nac',
	'String sexo',
	'ADMIN', true
);

INSERT INTO USER(ID, USERNAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, BIRTH_DATE, GENDER, USER_ROLE, ENABLED) VALUES (
	2, 
	'n2',
	'nombre 2',
	'apellidos 2',
	'correo 2',
	'{bcrypt}$2a$10$xLFtBIXGtYvAbRqM95JhcOaG23fHRpDoZIJrsF2cCff9xEHTTdK1u',
	'fecha_nac 2',
	'String sexo 2', 
	'USER',true
);

INSERT INTO USER(ID, USERNAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, BIRTH_DATE, GENDER, USER_ROLE, ENABLED) VALUES (
	3, 
	'n3',
	'nombre 3',
	'apellidos 3',
	'correo 3',
	'{bcrypt}$2a$10$xLFtBIXGtYvAbRqM95JhcOaG23fHRpDoZIJrsF2cCff9xEHTTdK1u',
	'fecha_nac 3',
	'String sexo 3', 
	'MOD',true
);

INSERT INTO EVENT(ID, NAME, DESCRIPTION, LOCATION, DATE, PUBLICATION_DATE, IS_APPROPRIATE, CREATOR_ID) VALUES (
	1,
	'Nombre Evento 1',
	'Descripcion Evnto 1',
	'El retiro',
	NOW(),
	NOW(),
	true,
	1
);

INSERT INTO EVENT(ID, NAME, DESCRIPTION, LOCATION, DATE, PUBLICATION_DATE, CREATOR_ID) VALUES (
	2,
	'Nombre Evento 2',
	'Descripcion Evnto 2',
	'El retiro',
	NOW(),
	NOW(),
	2
);

INSERT INTO EVENT(ID, NAME, DESCRIPTION, LOCATION, DATE, PUBLICATION_DATE, CREATOR_ID) VALUES (
	3,
	'Nombre Evento 3',
	'Descripcion Evnto 3',
	'El retiro',
	NOW(),
	NOW(),
	3
);

INSERT INTO EVENT(ID, NAME, DESCRIPTION, LOCATION, DATE, PUBLICATION_DATE, CREATOR_ID) VALUES (
	4,
	'Nombre Evento 4',
	'Descripcion Evnto 4',
	'El retiro',
	NOW(),
	NOW(),
	1
);

INSERT INTO EVENT(ID, NAME, DESCRIPTION, LOCATION, DATE, PUBLICATION_DATE) VALUES (
	5,
	'Nombre Evento 5',
	'Descripcion Evnto 5',
	'El retiro',
	NOW(),
	NOW()
);

INSERT INTO EVENT(ID, NAME, DESCRIPTION, LOCATION, DATE, PUBLICATION_DATE) VALUES (
	6,
	'Nombre Evento 6',
	'Descripcion Evnto 6',
	'El retiro',
	NOW(),
	NOW()
);

INSERT INTO EVENT(ID, NAME, DESCRIPTION, LOCATION, DATE, PUBLICATION_DATE) VALUES (
	7,
	'Nombre Evento 7',
	'Descripcion Evnto 7',
	'El retiro',
	NOW(),
	NOW()
);


INSERT INTO TAGS (ID, IS_CATEGORY, TAG) VALUES(1, true, 'sport');
INSERT INTO TAGS (ID, IS_CATEGORY, TAG) VALUES(2, true, 'game')
INSERT INTO EVENT_TAGS(EVENTS_ID, TAGS_ID) VALUES(1, 1);
INSERT INTO EVENT_TAGS(EVENTS_ID, TAGS_ID) VALUES(2, 1);
INSERT INTO EVENT_TAGS(EVENTS_ID, TAGS_ID) VALUES(3, 1);
INSERT INTO EVENT_TAGS(EVENTS_ID, TAGS_ID) VALUES(3, 2);

INSERT INT MESSAGE (ID, SENDER, RECEIVER, TEXT, LOCALDATETIME, READ) VALUES(
	1,
	1,
	2,
	"Hola, ¿qué tal?",
	NOW(),
	true
);

INSERT INT MESSAGE (ID, SENDER, RECEIVER, TEXT, LOCALDATETIME, READ) VALUES(
	2,
	2,
	1,
	"Bien, y tú?",
	NOW(),
	true
);

INSERT INT MESSAGE (ID, SENDER, RECEIVER, TEXT, LOCALDATETIME, READ) VALUES(
	3,
	1,
	2,
	"Yo también bien",
	NOW(),
	true
);

INSERT INT MESSAGE (ID, SENDER, RECEIVER, TEXT, LOCALDATETIME, READ) VALUES(
	4,
	1,
	3,
	"Eyyyyyy",
	NOW(),
	true
);

INSERT INT MESSAGE (ID, SENDER, RECEIVER, TEXT, LOCALDATETIME, READ) VALUES(
	5,
	3,
	1,
	"¿Quién eres?",
	NOW(),
	true
);