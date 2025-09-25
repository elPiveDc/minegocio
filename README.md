# Gestor de Franquicias

Aplicación web desarrollada en **Spring Boot (JDK 23)** con **Thymeleaf, HTML y Bootstrap**, que permite gestionar franquicias (negocios) y su configuración sobre una **base de datos MySQL centralizada**.

El sistema está diseñado para administrar usuarios, franquicias y las bases de datos asociadas a cada franquicia, permitiendo la conexión con distintos motores de base de datos (MySQL, PostgreSQL, Oracle, MongoDB, Cassandra).

---

## Tecnologías utilizadas

- **Java 23 (JDK)**
- **Spring Boot**
- **Thymeleaf**
- **Bootstrap**
- **Hibernate / JPA**
- **MySQL 8.0+**

---

## Base de datos principal (script completo)

```sql

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

-- ============================================================
--  TABLA: Preguntas Frecuentes
-- ============================================================

CREATE TABLE faq (
    id_faq BIGINT AUTO_INCREMENT PRIMARY KEY,
    question VARCHAR(500) NOT NULL,
    answer TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FULLTEXT idx_faq_fulltext (question, answer)  -- aquí mismo
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ============================================================
--  TABLA: documentos
-- ============================================================

CREATE TABLE documento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    slug VARCHAR(100) UNIQUE NOT NULL, -- Ej: 'terminos', 'privacidad'
    tipo_contenido VARCHAR(100) NOT NULL, -- Ej: 'application/pdf', 'application/msword'
    archivo LONGBLOB NOT NULL,
    fecha_subida TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

```
---
## Configuración (application.properties)

```properties

# Nombre de la aplicacion
spring.application.name=MiNegocio

# Conexion BD Central (cambiar el usuario y el pass)
spring.datasource.url=jdbc:mysql://localhost:3306/sistema_franquicias?useSSL=false&serverTimezone=UTC
spring.datasource.username=Prueba
spring.datasource.password=Prueba123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Valores para conexiones dinamicas (cambiar el usuario y el pass)
franquicia.db.host=localhost
franquicia.db.port=3306
franquicia.db.username=Prueba
franquicia.db.password=Prueba123

# Configuracion JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

# Logs mas detallados
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Configuracion de subida de archivos
spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=20MB

# Seguridad deshabilitada
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration

```
## Requisitos del sistema:
- Java 23 (JDK)
- Maven 3+
- Spring Boot
- MySQL 8.0+
- Navegador web moderno (para usar la interfaz con Thymeleaf + Bootstrap)

---
## Instalación y ejecución

Clonar el repositorio:

```bash
git clone https://github.com/tu_usuario/gestor-franquicias.git
cd gestor-franquicias
```

Crear la base de datos en MySQL ejecutando el script SQL proporcionado en este README.

Configurar el archivo application.properties con tu usuario y contraseña de MySQL.

Compilar y ejecutar la aplicación:

```bash
mvn spring-boot:run
```

Acceder en el navegador:

http://localhost:8080

---
## Funcionalidades actuales

- Registro de usuario + franquicia + BD
- Contraseñas encriptadas con BCrypt
- Validación de credenciales y login
- Sesión activa con HttpSession
- Redirección automática al dashboard
- Desconexión con /logout


