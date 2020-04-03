-- 
-- El contenido de este fichero se cargará al arrancar la aplicación, suponiendo que uses
-- 		application-default ó application-externaldb en modo 'create'
--

INSERT INTO usuario(id,nombre,apellidos,correo,password,fecha_nac,sexo,roles,enabled) VALUES (
	1, 
	'nombre',
	'apellidos',
	'correo',
	'{bcrypt}$2a$10$xLFtBIXGtYvAbRqM95JhcOaG23fHRpDoZIJrsF2cCff9xEHTTdK1u',
	'fecha_nac',
	'String sexo', 
	'ADMIN',true
);

INSERT INTO usuario(id,nombre,apellidos,correo,password,fecha_nac,sexo,roles,enabled) VALUES (
	2, 
	'nombre 2',
	'apellidos 2',
	'correo 2',
	'{bcrypt}$2a$10$xLFtBIXGtYvAbRqM95JhcOaG23fHRpDoZIJrsF2cCff9xEHTTdK1u',
	'fecha_nac 2',
	'String sexo 2', 
	'USER',true
);

INSERT INTO usuario(id,nombre,apellidos,correo,password,fecha_nac,sexo,roles,enabled) VALUES (
	3, 
	'nombre 3',
	'apellidos 3',
	'correo 3',
	'{bcrypt}$2a$10$xLFtBIXGtYvAbRqM95JhcOaG23fHRpDoZIJrsF2cCff9xEHTTdK1u',
	'fecha_nac 3',
	'String sexo 3', 
	'USER',true
);

INSERT INTO evento(id,nombre,descripcion,ubicacion,fecha,publicada) VALUES (
	1,
	'Nombre Evento',
	'Descripcion Evnto',
	'El retiro',
	NOW(),
	NOW()
);
INSERT INTO evento(id,nombre,descripcion,ubicacion,fecha,publicada) VALUES (
	2,
	'Nombre Evento 2',
	'Descripcion Evnto 2',
	'El retiro',
	NOW(),
	NOW()
);
INSERT INTO evento(id,nombre,descripcion,ubicacion,fecha,publicada) VALUES (
	3,
	'Nombre Evento 3',
	'Descripcion Evnto 3',
	'El retiro',
	NOW(),
	NOW()
);
