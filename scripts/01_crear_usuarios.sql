-- 1. Actualizamos al admin para asegurarnos que el rol esté bien escrito
UPDATE usuarios 
SET rol = 'Administrador' 
WHERE username = 'admin';

-- 2. Insertamos un usuario nuevo con el rol de 'Analista'
-- (Si ya existe, puedes cambiarle el nombre a 'analista_juan' o similar)
INSERT INTO usuarios (username, password, rol) 
VALUES ('analista1', '1234', 'Analista');

-- 3. Verificamos cómo quedó la tabla
SELECT * FROM usuarios;