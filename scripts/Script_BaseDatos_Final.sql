-- =============================================
-- SCRIPT FINAL DEFINITIVO - SISTEMA LICENCIAS V2.0
-- Fecha: Enero 2026
-- Autores: Juan Daniel & Majo
-- =============================================

-- 1. LIMPIEZA TOTAL (Eliminar versiones anteriores)
DROP TABLE IF EXISTS licencias CASCADE;
DROP TABLE IF EXISTS pruebas_psicometricas CASCADE;
DROP TABLE IF EXISTS conductores CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;

-- 2. TABLA DE USUARIOS (Seguridad y Roles)
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL
);

-- 3. TABLA DE CONDUCTORES (Perfil del Ciudadano)
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

-- 4. TABLA DE PRUEBAS PSICOMÉTRICAS (Evaluaciones)
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

-- 5. TABLA DE LICENCIAS (Emisión de Documentos)
CREATE TABLE licencias (
    id SERIAL PRIMARY KEY,
    conductor_id INT NOT NULL,
    numero_licencia VARCHAR(50),
    -- IMPORTANTE: Se amplió a 150 caracteres para descripciones largas
    tipo_licencia VARCHAR(150) NOT NULL,
    fecha_emision DATE DEFAULT CURRENT_DATE,
    fecha_vencimiento DATE NOT NULL,
    activa BOOLEAN DEFAULT TRUE,
    prueba_psicometrica_id BIGINT,
    observaciones TEXT,
    precio DECIMAL(10,2),
    CONSTRAINT fk_conductor FOREIGN KEY (conductor_id) REFERENCES conductores(id)
);

-- 6. DATOS INICIALES (Semilla)
INSERT INTO usuarios (username, password, rol) VALUES ('admin', '1234', 'Administrador');
INSERT INTO usuarios (username, password, rol) VALUES ('analista1', '1234', 'Analista');