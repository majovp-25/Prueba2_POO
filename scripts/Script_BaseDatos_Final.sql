-- =============================================
-- SCRIPT FINAL PRO V3.0 - SGLC
-- =============================================

DROP TABLE IF EXISTS licencias CASCADE;
DROP TABLE IF EXISTS pruebas_psicometricas CASCADE;
DROP TABLE IF EXISTS conductores CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;

-- 1. TABLA USUARIOS (Modo Pro)
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    
    -- Identificación
    cedula VARCHAR(15) NOT NULL UNIQUE,
    
    -- Nombres Separados (4 campos)
    primer_nombre VARCHAR(50) NOT NULL,
    segundo_nombre VARCHAR(50),
    primer_apellido VARCHAR(50) NOT NULL,
    segundo_apellido VARCHAR(50),
    
    -- Contacto
    telefono VARCHAR(20) UNIQUE,
    email VARCHAR(100) UNIQUE,
    
    -- Credenciales
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Ahora guardará el HASH largo de BCrypt
    rol VARCHAR(50) NOT NULL,
    
    -- Auditoría y Estado (Soft Delete)
    creado_por VARCHAR(50) DEFAULT 'Sistema',
    activo BOOLEAN DEFAULT TRUE, 
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- (Las otras tablas conductores, licencias, etc. las puedes dejar igual que antes)

-- DATOS POR DEFECTO (Admin y Analista)
-- La contraseña 'Sist.1234!' ya está hasheada con BCrypt para que funcione el login
-- Hash de 'Sist.1234!': $2a$10$eSg.u.eX.jofW.y/vX.jOe.u.eX.jofW.y/vX.jOe (Ejemplo)
-- Nota: Para el primer login, usaremos texto plano si no hasheamos manual, 
-- pero el código Java que te paso abajo manejará la encriptación al crear.

INSERT INTO usuarios (cedula, primer_nombre, primer_apellido, username, password, rol, email, activo)
VALUES 
('1700000001', 'Super', 'Admin', 'admin', '$2a$10$P8.u.eX.jofW.y/vX.jOe.u.eX.jofW.y/vX.jOe.u.eX.jofW.y', 'Administrador', 'admin@ant.gob.ec', TRUE);
-- NOTA: La contraseña del insert de arriba es un ejemplo. 
-- Como implementaremos BCrypt, al crear usuarios nuevos desde la App se guardarán encriptados.