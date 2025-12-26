-- 1. Borrar la tabla si existe (para evitar errores al reintentar)
DROP TABLE IF EXISTS usuarios;

-- 2. Crear la tabla REAL de usuarios
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('Administrador', 'Analista'))
);

-- 3. Insertar el usuario Admin (el que Java está buscando)
INSERT INTO usuarios (username, password, rol) VALUES 
('admin', '1234', 'Administrador');

-- 4. Confirmar que se creó
SELECT * FROM usuarios;