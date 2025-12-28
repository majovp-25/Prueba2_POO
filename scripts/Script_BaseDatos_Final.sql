-- =============================================
-- SCRIPT BASE DE DATOS - SISTEMA LICENCIAS
-- Autores: Juan Daniel y Majo
-- Motor: PostgreSQL
-- =============================================

-- 1. CREACIÓN DE TABLAS

-- Tabla de Usuarios (Para el Login)
CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL
);

-- Tabla de Conductores
CREATE TABLE IF NOT EXISTS conductores (
    id SERIAL PRIMARY KEY,
    cedula VARCHAR(10) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(15),
    email VARCHAR(100)
);

-- Tabla de Licencias (Relacionada con Conductores)
CREATE TABLE IF NOT EXISTS licencias (
    id SERIAL PRIMARY KEY,
    conductor_id INT NOT NULL,
    tipo_licencia VARCHAR(5) NOT NULL, 
    fecha_emision TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_caducidad TIMESTAMP NOT NULL,
    estado VARCHAR(20) DEFAULT 'VIGENTE',
    precio DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_conductor FOREIGN KEY (conductor_id) REFERENCES conductores(id)
);

-- 2. INSERCIÓN DE DATOS INICIALES

-- Usuario Administrador por defecto
INSERT INTO usuarios (username, password, rol) 
VALUES ('admin', '1234', 'Administrador');

-- Usuario Analista por defecto
INSERT INTO usuarios (username, password, rol) 
VALUES ('analista1', '1234', 'Analista');
