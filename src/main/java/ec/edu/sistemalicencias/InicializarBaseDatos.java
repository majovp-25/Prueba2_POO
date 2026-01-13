package ec.edu.sistemalicencias;

import ec.edu.sistemalicencias.config.DatabaseConfig;
import org.mindrot.jbcrypt.BCrypt; // Necesario para generar el hash correcto

import java.sql.Connection;
import java.sql.Statement;

public class InicializarBaseDatos {

    public static void main(String[] args) {
        System.out.println("REINICIANDO BASE DE DATOS (V3.0 PRO)...");
        
        // 1. GENERAMOS EL HASH REAL DE "Sist.1234!"
        // Esto asegura que la contraseña en la BD coincida con la que escribes
        String passPlano = "Sist.1234!";
        String passHash = BCrypt.hashpw(passPlano, BCrypt.gensalt());
        
        System.out.println("🔐 Contraseña por defecto: " + passPlano);
        System.out.println("🔐 Hash generado para BD: " + passHash);

        String sql = """
            -- 1. LIMPIEZA
            DROP TABLE IF EXISTS licencias CASCADE;
            DROP TABLE IF EXISTS pruebas_psicometricas CASCADE;
            DROP TABLE IF EXISTS conductores CASCADE;
            DROP TABLE IF EXISTS usuarios CASCADE;

            -- 2. TABLA USUARIOS (V3.0)
            CREATE TABLE usuarios (
                id SERIAL PRIMARY KEY,
                cedula VARCHAR(15) NOT NULL UNIQUE,
                primer_nombre VARCHAR(50) NOT NULL,
                segundo_nombre VARCHAR(50),
                primer_apellido VARCHAR(50) NOT NULL,
                segundo_apellido VARCHAR(50),
                telefono VARCHAR(20),
                email VARCHAR(100) UNIQUE,
                username VARCHAR(50) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL, 
                rol VARCHAR(50) NOT NULL,
                creado_por VARCHAR(50) DEFAULT 'Sistema',
                activo BOOLEAN DEFAULT TRUE, 
                fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );

            -- 3. TABLA CONDUCTORES
            CREATE TABLE conductores (
                id SERIAL PRIMARY KEY,
                cedula VARCHAR(15) NOT NULL UNIQUE,
                nombres VARCHAR(100) NOT NULL,
                apellidos VARCHAR(100) NOT NULL,
                fecha_nacimiento DATE NOT NULL,
                direccion VARCHAR(200),
                telefono VARCHAR(15),
                email VARCHAR(100),
                tipo_sangre VARCHAR(10),
                donante BOOLEAN DEFAULT FALSE,
                documentos_validados BOOLEAN DEFAULT FALSE,
                observaciones TEXT
            );

            -- 4. TABLA PRUEBAS
            CREATE TABLE pruebas_psicometricas (
                id SERIAL PRIMARY KEY,
                conductor_id INT NOT NULL,
                fecha_prueba TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                puntaje_visual DECIMAL(5,2),
                puntaje_auditivo DECIMAL(5,2),
                puntaje_motor DECIMAL(5,2),
                puntaje_psicologico DECIMAL(5,2),
                puntaje_reaccion DECIMAL(5,2),
                observaciones TEXT,
                estado VARCHAR(20) DEFAULT 'Pendiente',
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
                creado_por VARCHAR(50),
                CONSTRAINT fk_conductor FOREIGN KEY (conductor_id) REFERENCES conductores(id)
            );
        """;

        // 6. SQL PARA INSERTAR LOS USUARIOS CON EL HASH GENERADO
        // Concatenamos la variable passHash dentro de la cadena SQL
        String inserts = String.format("""
            INSERT INTO usuarios (cedula, primer_nombre, primer_apellido, username, password, rol, email, activo)
            VALUES 
            ('1700000001', 'Super', 'Admin', 'admin', '%s', 'Administrador', 'admin@ant.gob.ec', TRUE),
            ('1700000002', 'Juan', 'Analista', 'analista1', '%s', 'Analista', 'juan@ant.gob.ec', TRUE);
            """, passHash, passHash);

        try (Connection conn = DatabaseConfig.getInstance().obtenerConexion();
            Statement stmt = conn.createStatement()) {

            // Ejecutar creación de tablas
            stmt.executeUpdate(sql);
            
            // Ejecutar inserts
            stmt.executeUpdate(inserts);

            System.out.println("✅ ¡BASE DE DATOS REINICIADA EXITOSAMENTE!");
            System.out.println("✅ Usuario 'admin' configurado con clave: " + passPlano);
            System.out.println("✅ Usuario 'analista1' configurado con clave: " + passPlano);

        } catch (Exception e) {
            System.err.println("❌ Error crítico: " + e.getMessage());
            e.printStackTrace();
        }
    }
}