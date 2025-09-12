-- ============================================================
--  BASE DE DATOS CENTRAL - OPTIMIZADA
-- ============================================================

-- Crear base de datos principal
CREATE DATABASE IF NOT EXISTS sistema_franquicias
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sistema_franquicias;

-- ============================================================
--  TABLA: USUARIOS (DUEÑOS DE FRANQUICIA)
-- ============================================================
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('ACTIVO','INACTIVO','BLOQUEADO') DEFAULT 'ACTIVO'
) ENGINE=InnoDB;

CREATE INDEX idx_usuarios_correo ON usuarios(correo);

-- ============================================================
--  TABLA: FRANQUICIAS
-- ============================================================
CREATE TABLE IF NOT EXISTS franquicias (
    id_franquicia INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    nombre_franquicia VARCHAR(100) NOT NULL UNIQUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('ACTIVA','INACTIVA','ELIMINADA') DEFAULT 'ACTIVA',
    created_by INT NULL,
    updated_by INT NULL,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_franquicias_usuario ON franquicias(id_usuario);

-- ============================================================
--  TABLA: BASES DE DATOS DE CADA FRANQUICIA
-- ============================================================
CREATE TABLE IF NOT EXISTS bases_datos_franquicia (
    id_bd INT AUTO_INCREMENT PRIMARY KEY,
    id_franquicia INT NOT NULL,
    nombre_bd VARCHAR(100) NOT NULL,
    tipo_bd ENUM('MYSQL','POSTGRESQL','ORACLE','MONGODB','CASSANDRA') NOT NULL,
    estado ENUM('CONFIGURADA','NO_CONFIGURADA','ERROR') DEFAULT 'NO_CONFIGURADA',
    url_conexion TEXT,
    usuario_conexion VARCHAR(100),
    pass_conexion_hash VARCHAR(255),
    created_by INT NULL,
    updated_by INT NULL,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_franquicia) REFERENCES franquicias(id_franquicia)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_bd_franquicia ON bases_datos_franquicia(id_franquicia);

-- ============================================================
--  TABLA: OBJETOS CREADOS EN CADA BASE DE DATOS DE FRANQUICIA
-- ============================================================
CREATE TABLE IF NOT EXISTS objetos_bd_franquicia (
    id_objeto INT AUTO_INCREMENT PRIMARY KEY,
    id_bd INT NOT NULL,
    nombre_tabla VARCHAR(100) NOT NULL,
    tipo_objeto ENUM('TABLA','VISTA','FUNCION') DEFAULT 'TABLA',
    es_tabla_usuarios BOOLEAN DEFAULT FALSE,
    columnas JSON NOT NULL, -- Ahora es JSON para consultas más potentes
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INT NULL,
    updated_by INT NULL,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_bd) REFERENCES bases_datos_franquicia(id_bd)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_objetos_bd ON objetos_bd_franquicia(id_bd);
