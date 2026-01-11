package ec.edu.sistemalicencias;

import ec.edu.sistemalicencias.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.Statement;

public class InicializarBaseDatos {

    public static void main(String[] args) {
        System.out.println("REINICIANDO BASE DE DATOS MAESTRA (V4.0 - FINAL)...");
        System.out.println("ADVERTENCIA: Se borrarán todos los datos existentes.");

        String sql = """
            -- 1. LIMPIEZA TOTAL
            DROP TABLE IF EXISTS licencias CASCADE;
            DROP TABLE IF EXISTS pruebas_psicometricas CASCADE;
            DROP TABLE IF EXISTS conductores CASCADE;
            DROP TABLE IF EXISTS usuarios CASCADE;

            -- 2. TABLA USUARIOS (ACTUALIZADA CON AUDITORÍA)
            CREATE TABLE usuarios (
                id SERIAL PRIMARY KEY,
                nombres   VARCHAR(100) NOT NULL,
                apellidos VARCHAR(100) NOT NULL,
                telefono  VARCHAR(15),
                email     VARCHAR(100) UNIQUE,
                username  VARCHAR(50) NOT NULL UNIQUE,
                password  VARCHAR(255) NOT NULL,
                rol       VARCHAR(50) NOT NULL,
                creado_por VARCHAR(50) DEFAULT 'Sistema' -- <--- ¡AQUÍ ESTÁ LA MAGIA!
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

            -- 6. DATOS BASE (CON CONTRASEÑA CORRECTA Y CAMPO CREADO_POR)
            -- Admin
            INSERT INTO usuarios (username, password, rol, nombres, apellidos, telefono, email, creado_por)
            VALUES ('admin', 'Sist.1234!', 'Administrador', 'María José', 'Paredes', '0968830302', 'mariaparedes@gmail.com', 'Sistema');
                
            -- Analista
            INSERT INTO usuarios (username, password, rol, nombres, apellidos, telefono, email, creado_por)
            VALUES ('analista1', 'Sist.1234!', 'Analista', 'Juan', 'Vasquez', '0963218871', 'juanvasquez@gmail.com', 'Sistema');
        """;

        try (Connection conn = DatabaseConfig.getInstance().obtenerConexion();
            Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
            System.out.println("✅ ¡BASE DE DATOS REINICIADA Y ACTUALIZADA!");
            System.out.println("✅ Estructura correcta (incluye 'creado_por').");
            System.out.println("✅ Usuarios 'admin' y 'analista1' reseteados con password 'Sist.1234!'.");

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}