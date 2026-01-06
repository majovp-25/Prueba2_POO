package ec.edu.sistemalicencias;

import ec.edu.sistemalicencias.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.Statement;

public class ActualizarBaseDatos {

    public static void main(String[] args) {
        System.out.println("🛠️ APLICANDO PARCHE DE SEGURIDAD (SIN BORRAR DATOS)...");

        // Este SQL usa "ALTER TABLE" en vez de "DROP", así salvamos la info.
        String sql = """
            -- 1. Agregar columnas nuevas permitiendo nulos temporalmente
            ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS nombres VARCHAR(100);
            ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS apellidos VARCHAR(100);
            ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS telefono VARCHAR(15);
            ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS email VARCHAR(100);

            -- 2. Rellenar datos vacíos en usuarios viejos (IMPORTANTE para evitar errores)
            UPDATE usuarios SET nombres = 'Usuario', apellidos = 'Existente' WHERE nombres IS NULL;
            UPDATE usuarios SET telefono = '0000000000' WHERE telefono IS NULL;
            UPDATE usuarios SET email = CONCAT(username, '@sistema.com') WHERE email IS NULL;

            -- 3. Ahora sí, obligar a que sean NOT NULL
            ALTER TABLE usuarios ALTER COLUMN nombres SET NOT NULL;
            ALTER TABLE usuarios ALTER COLUMN apellidos SET NOT NULL;
            
            -- 4. Agregar restricción UNIQUE al email
            ALTER TABLE usuarios DROP CONSTRAINT IF EXISTS usuarios_email_key;
            ALTER TABLE usuarios ADD CONSTRAINT usuarios_email_key UNIQUE (email);

            -- 5. Crear las otras tablas si por casualidad no existen (sin borrar nada)
            CREATE TABLE IF NOT EXISTS conductores (
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
            
            -- (Nota: Para Licencias y Pruebas asumimos que ya existen o se crean con el script inicial si es primera vez)
        """;

        try (Connection conn = DatabaseConfig.getInstance().obtenerConexion();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
            System.out.println("✅ ¡PARCHE APLICADO!");
            System.out.println("✅ Tus usuarios anteriores siguen ahí, ahora tienen campos nuevos.");
            System.out.println("✅ Los usuarios viejos se llaman 'Usuario Existente' (puedes editarlos en la app).");

        } catch (Exception e) {
            System.err.println("❌ Error al parchear: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
