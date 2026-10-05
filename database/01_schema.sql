-- Base de datos: cosecha_comun (PostgreSQL / Neon). Fase 1: Login + Menú
-- Ejecutar conectado a la base de datos creada en Neon.

DROP TABLE IF EXISTS usuarios CASCADE;
DROP TABLE IF EXISTS permiso CASCADE;
DROP TABLE IF EXISTS rol CASCADE;

CREATE TABLE rol (
    rol_id              SERIAL PRIMARY KEY,
    nombre_rol          VARCHAR(30)  NOT NULL UNIQUE,   -- CAFICULTOR, COMPRADOR, ADMINISTRADOR
    descripcion         VARCHAR(100),
    fecha_creacion      TIMESTAMP    NOT NULL DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP,
    usuario_registro    VARCHAR(50)  NOT NULL DEFAULT 'sistema',
    estado              VARCHAR(15)  NOT NULL DEFAULT 'Activo' CHECK (estado IN ('Activo','Inactivo'))
);

CREATE TABLE permiso (
    permiso_id          SERIAL PRIMARY KEY,
    nombre_permiso      VARCHAR(30)  NOT NULL UNIQUE,
    descripcion         VARCHAR(100),
    fecha_creacion      TIMESTAMP    NOT NULL DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP,
    usuario_registro    VARCHAR(50)  NOT NULL DEFAULT 'sistema',
    estado              VARCHAR(15)  NOT NULL DEFAULT 'Activo' CHECK (estado IN ('Activo','Inactivo'))
);

CREATE TABLE usuarios (
    id_usuario          SERIAL PRIMARY KEY,
    nombre_usuario      VARCHAR(50)  NOT NULL UNIQUE,
    contrasena          VARCHAR(255) NOT NULL,          -- hash BCrypt
    rol_id              INT          NOT NULL REFERENCES rol(rol_id),
    permiso_id          INT          NOT NULL REFERENCES permiso(permiso_id),
    ultimo_acceso       TIMESTAMP,
    intentos_fallidos   INT          NOT NULL DEFAULT 0,
    bloqueado           BOOLEAN      NOT NULL DEFAULT FALSE,
    token_recuperacion  VARCHAR(255),
    fecha_creacion      TIMESTAMP    NOT NULL DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP,
    usuario_registro    VARCHAR(50)  NOT NULL DEFAULT 'sistema',
    estado              VARCHAR(15)  NOT NULL DEFAULT 'Activo' CHECK (estado IN ('Activo','Inactivo'))
);
