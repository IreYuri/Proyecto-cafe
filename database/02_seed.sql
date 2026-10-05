INSERT INTO rol (nombre_rol, descripcion) VALUES
 ('CAFICULTOR',    'Productor que vende lotes de café verde y puede pedir micropréstamos'),
 ('COMPRADOR',     'Compra lotes de café verde publicados por los caficultores'),
 ('ADMINISTRADOR', 'Administra la comunidad, usuarios y evalúa solicitudes');

INSERT INTO permiso (nombre_permiso, descripcion) VALUES
 ('TOTAL',          'Lectura y escritura en todo el sistema'),
 ('GESTION_LOTES',  'Publicar y administrar lotes y solicitudes propias'),
 ('COMPRA',         'Explorar lotes y registrar compras');

-- Contraseña de prueba para los 3 usuarios: Admin123*
INSERT INTO usuarios (nombre_usuario, contrasena, rol_id, permiso_id) VALUES
 ('admin',       '$2a$10$MWIoicXAnH4rupjfF9/WruVr012Rw2iMczrKw5OhSJR4OUgzPW2C6', (SELECT rol_id FROM rol WHERE nombre_rol='ADMINISTRADOR'), (SELECT permiso_id FROM permiso WHERE nombre_permiso='TOTAL')),
 ('caficultor1', '$2a$10$MWIoicXAnH4rupjfF9/WruVr012Rw2iMczrKw5OhSJR4OUgzPW2C6', (SELECT rol_id FROM rol WHERE nombre_rol='CAFICULTOR'),    (SELECT permiso_id FROM permiso WHERE nombre_permiso='GESTION_LOTES')),
 ('comprador1',  '$2a$10$MWIoicXAnH4rupjfF9/WruVr012Rw2iMczrKw5OhSJR4OUgzPW2C6', (SELECT rol_id FROM rol WHERE nombre_rol='COMPRADOR'),     (SELECT permiso_id FROM permiso WHERE nombre_permiso='COMPRA'));

-- Para desbloquear un usuario durante las pruebas:
-- UPDATE usuarios SET bloqueado=FALSE, intentos_fallidos=0 WHERE nombre_usuario='admin';
