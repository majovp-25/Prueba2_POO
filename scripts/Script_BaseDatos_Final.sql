-- =============================================
-- SCRIPT FINAL DEFINITIVO - SISTEMA LICENCIAS V2.1
-- Fecha: 5 de Enero 2026
-- Autores: Juan Daniel & Majo
-- =============================================

-- 1. LIMPIEZA
DROP TABLE IF EXISTS licencias CASCADE;
DROP TABLE IF EXISTS pruebas_psicometricas CASCADE;
DROP TABLE IF EXISTS conductores CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;

-- 2. TABLA USUARIOS
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombres   VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono  VARCHAR(15),
    email     VARCHAR(100) UNIQUE,
    username  VARCHAR(50) NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    rol       VARCHAR(50) NOT NULL
);

-- 3. TABLA CONDUCTORES
CREATE TABLE conductores (
    id SERIAL PRIMARY KEY,
    cedula VARCHAR(10) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(15),
    email VARCHAR(100),
    tipo_sangre VARCHAR(10),
    documentos_validados BOOLEAN DEFAULT FALSE,
    observaciones TEXT
);

-- 4. TABLA PRUEBAS PSICOMÉTRICAS
CREATE TABLE pruebas_psicometricas (
    id SERIAL PRIMARY KEY,
    conductor_id INT NOT NULL,
    fecha_prueba TIMESTAMP,
    puntaje_visual DECIMAL(5,2),
    puntaje_auditivo DECIMAL(5,2),
    puntaje_motor DECIMAL(5,2),
    puntaje_psicologico DECIMAL(5,2),
    puntaje_reaccion DECIMAL(5,2),
    observaciones TEXT,
    CONSTRAINT fk_pruebas_conductor FOREIGN KEY (conductor_id) REFERENCES conductores(id)
);

-- 5. TABLA LICENCIAS
CREATE TABLE licencias (
    id SERIAL PRIMARY KEY,
    conductor_id INT NOT NULL,
    numero_licencia VARCHAR(50),
    tipo_licencia VARCHAR(150) NOT NULL,
    fecha_emision DATE DEFAULT CURRENT_DATE,
    fecha_vencimiento DATE NOT NULL,
    activa BOOLEAN DEFAULT TRUE,
    prueba_psicometrica_id BIGINT,
    observaciones TEXT,
    precio DECIMAL(10,2),
    CONSTRAINT fk_conductor FOREIGN KEY (conductor_id) REFERENCES conductores(id)
);

-- 6. DATOS BASE
INSERT INTO usuarios (username, password, rol, nombres, apellidos, telefono, email)
VALUES ('admin', '1234', 'Administrador', 'María José', 'Paredes', '0968830302', 'mariaparedes@gmail.com');

INSERT INTO usuarios (username, password, rol, nombres, apellidos, telefono, email)
VALUES ('analista1', '1234', 'Analista', 'Juan', 'Vasquez', '0963218871', 'juanvasquez@gmail.com');

-- 1. Crear la columna nueva
ALTER TABLE usuarios ADD COLUMN creado_por VARCHAR(50);

-- 2. Ponerle un valor por defecto a los que ya existen (para que no salga NULL)
UPDATE usuarios SET creado_por = 'Sistema' WHERE creado_por IS NULL;